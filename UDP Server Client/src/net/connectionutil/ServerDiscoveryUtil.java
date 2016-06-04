package net.connectionutil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;
import net.server.ServerInfo;

public class ServerDiscoveryUtil extends ConnectionUtil {

	public static Map<String, String> getAvailableServers(int timeout) throws SocketException {
		return getServerNames(getAvailableServerIPs(timeout,Config.PORT),Config.PORT);
	}

	public static List<String> getAvailableServerIPs(int timeout, int port) {
		List<String> availableServerIPs = new ArrayList<>();
		try {
			sendProbePacket(getBroadcastAddresses(),port);
			availableServerIPs = waitForServerResponses(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return availableServerIPs;
	}

	public static Map<String, String> getServerNames(List<String> ipsToCheck,int portToCheck) {
		Map<String, String> serverList = new HashMap<>();
		for (String serverAddress : ipsToCheck) {
			try {
				serverList.put(getServerName(serverAddress,portToCheck), serverAddress);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return serverList;
	}

	public static String getServerName(String serverAddress, int port) throws IOException {
		getUtilSocket().send(new SimpleExchangePacket(RequestType.SERVER_NAME, "").getPacket(serverAddress, port));
		DatagramPacket response = receivePacket();
		return new SimpleExchangePacket(response.getData()).getResponse().getResponseNote();
	}

	public static List<String> waitForServerResponses(int timeout) throws SocketException {
		Set<String> servers = new HashSet<>();
		DatagramPacket recievePacket;
		getUtilSocket().setSoTimeout(timeout);
		byte[] recvBuf = new byte[15000];
		while (true) {
			try {
				recievePacket = new DatagramPacket(recvBuf, recvBuf.length);
				getUtilSocket().receive(recievePacket);
				System.out.println("packet recieved!");
				if (new SimpleExchangePacket(recievePacket.getData()).getResponse().getResponseType()
						.equals(ResponseType.PROBE)) {
					servers.add(recievePacket.getAddress().getHostAddress());
				}
			} catch (IOException e) {
				System.out.println("time out reached. returning");
				System.out.println(servers);
				break;
			}
		}
		getUtilSocket().setSoTimeout(0);
		return new ArrayList<>(servers);
	}

	public static void sendProbePacket(String address, int port) {
		DatagramPacket packet = new SimpleExchangePacket(RequestType.PROBE, "").getPacket(address, port);
		try {
			getUtilSocket().send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendProbePacket(List<String> broadcastAddresses, int portToProbe) {
		for (String address : broadcastAddresses) {
			sendProbePacket(address, portToProbe);
		}
	}

	public static List<String> getBroadcastAddresses() throws SocketException {
		List<String> addresses = new ArrayList<>();
		addresses.add("255.255.255.255");
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp()) {
				continue; // Don't want to broadcast to the loopback
							// interface
			}

			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				InetAddress broadcast = interfaceAddress.getBroadcast();
				if (broadcast == null) {
					continue;
				}
				
				addresses.add(broadcast.getHostAddress());
			}
		}
		return addresses;

	}

	public static ServerInfo getServerInfo(String serverAddress,int port) throws IOException {
		getUtilSocket().send(new SimpleExchangePacket(RequestType.SERVER_INFO, "").getPacket(serverAddress, port));
		DatagramPacket response = receivePacket();
		return new Gson().fromJson(new SimpleExchangePacket(response.getData()).getResponse().getResponseNote(),
				ServerInfo.class);
	}

	public static boolean checkAddressForServer(String address,int port, int timeout) {
		sendProbePacket(address,port);
		DatagramPacket receivePacket = null;
		try {
			receivePacket = receivePacket(timeout);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (receivePacket == null)
			return false;
		return new SimpleExchangePacket(receivePacket.getData()).getResponse().getResponseType()
				.equals(ResponseType.PROBE);
	}
}