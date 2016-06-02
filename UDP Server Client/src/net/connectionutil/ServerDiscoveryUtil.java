package net.connectionutil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

public class ServerDiscoveryUtil extends ConnectionUtil{
	public ServerDiscoveryUtil(DatagramSocket socket) {
		super(socket);
	}


	public static void main(String[] args) throws SocketException {
		ServerDiscoveryUtil runner = new ServerDiscoveryUtil(new DatagramSocket());
		System.out.println(runner.getAvailableServers(1000));
	}


	public Map<String, InetAddress> getAvailableServers(int timeout) throws SocketException {
		return getServerNames(getAvailableServerIPs(1000));
	}

	public List<InetAddress> getAvailableServerIPs(int timeout) {
		List<InetAddress> availableServerIPs = new ArrayList<>();
		try {
			sendProbePacket(getBroadcastAddresses());
			availableServerIPs = waitForServerResponses(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return availableServerIPs;
	}

	private Map<String, InetAddress> getServerNames(List<InetAddress> ipsToCheck) {
		Map<String, InetAddress> serverList = new HashMap<>();
		for (InetAddress serverAddress : ipsToCheck) {
			try {
				serverList.put(getServerName(serverAddress), serverAddress);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return serverList;
	}

	private String getServerName(InetAddress serverAddress) throws IOException {
		socket.send(new SimpleExchangePacket(RequestType.SERVER_NAME, "").getPacket(serverAddress, 8888));
		DatagramPacket response = receivePacket();
		return new SimpleExchangePacket(response.getData()).getResponse().getResponseNote();
	}

	private List<InetAddress> waitForServerResponses(int timeout) throws SocketException {
		Set<InetAddress> servers = new HashSet<>();
		DatagramPacket recievePacket;
		socket.setSoTimeout(timeout);
		byte[] recvBuf = new byte[15000];
		while (true) {
			try {
				recievePacket = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(recievePacket);
				System.out.println("packet recieved!");
				if(new SimpleExchangePacket(recievePacket.getData()).getResponse().getResponseType().equals(ResponseType.PROBE)){
					servers.add(recievePacket.getAddress());
				}
			} catch (IOException e) {
				System.out.println("time out reached. returning");
				System.out.println(servers);
				break;
			}
		}
		socket.setSoTimeout(0);
		return new ArrayList<>(servers);
	}

	private void sendProbePacket(InetAddress address) {
		DatagramPacket packet = new SimpleExchangePacket(RequestType.PROBE, "").getPacket(address, 8888);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendProbePacket(List<InetAddress> broadcastAddresses) {
		for (InetAddress address : broadcastAddresses) {
			sendProbePacket(address);
		}
	}

	private List<InetAddress> getBroadcastAddresses() throws SocketException {
		List<InetAddress> addresses = new ArrayList<>();
		try {
			addresses.add(InetAddress.getByName("255.255.255.255"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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
				addresses.add(broadcast);
			}
		}
		return addresses;

	}
}