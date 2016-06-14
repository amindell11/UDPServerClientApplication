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
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest.RequestType;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse.ResponseType;
import net.server.ServerInfo;

public class ServerDiscoveryUtil extends ConnectionUtil {

    public static Map<String, String> getAvailableServers(int timeout) {
	List<String> availableServerIPs = getAvailableServerIPs(timeout, Config.PORT);
	return getServerNames(availableServerIPs, Config.PORT);
    }

    public static List<String> getAvailableServerIPs(int timeout, int port) {
	List<String> availableServerIPs = new ArrayList<>();
	try {
	    sendProbePackets(getBroadcastAddresses(), port);
	    availableServerIPs = waitForServerResponses(timeout);
	} catch (SocketException e) {
	    e.printStackTrace();
	}
	return availableServerIPs;
    }

    public static Map<String, String> getServerNames(List<String> ipsToCheck, int portToCheck) {
	Map<String, String> serverList = new HashMap<>();
	for (String serverAddress : ipsToCheck) {
	    try {
		serverList.put(getServerName(serverAddress, portToCheck), serverAddress);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return serverList;
    }

    public static String getServerName(String serverAddress, int port) throws IOException {
	Exchange message = Exchange.newBuilder().setExtension(SimpleExchange.simpleExchange, SimpleExchange.newBuilder().setRequest(SimpleExchangeRequest.newBuilder().setRequestType(RequestType.SERVER_NAME).build()).build()).build();
	ConnectionUtil.sendMessage(message, getUtilSocket(), serverAddress, port);
	SimpleExchangeResponse response = receiveMessage(getUtilSocket()).getExtension(SimpleExchange.simpleExchange).getResponse();
	return response.getResponseNote();
    }

    public static List<String> waitForServerResponses(int timeout) {
	Set<String> servers = new HashSet<>();
	while (true) {
	    DatagramPacket receivePacket = receivePacket(getUtilSocket(), timeout);
	    if (receivePacket == null) {
		System.out.println("time out reached. returning");
		break;
	    }
	    Exchange receiveMessage = convertMessage(receivePacket.getData());
	    if (receiveMessage.getExtension(SimpleExchange.simpleExchange).getResponse().getResponseType().equals(ResponseType.PROBE)) {
		servers.add(receivePacket.getAddress().getHostAddress());
	    }
	}
	return new ArrayList<>(servers);
    }

    public static void sendProbePacket(String address, int port) {
	try {
	    ConnectionUtil.sendRequest(RequestType.PROBE, "", 0, getUtilSocket(), address, port);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void sendProbePackets(List<String> broadcastAddresses, int portToProbe) {
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

    public static ServerInfo getServerInfo(String serverAddress, int port) throws IOException {
	ConnectionUtil.sendRequest(RequestType.SERVER_INFO, "", 0, getUtilSocket(), serverAddress, port);
	SimpleExchangeResponse response = receiveMessage(getUtilSocket()).getExtension(SimpleExchange.simpleExchange).getResponse();
	return new Gson().fromJson(response.getResponseNote(), ServerInfo.class);
    }

    public static boolean checkAddressForServer(String address, int port, int timeout) {
	sendProbePacket(address, port);
	Exchange receiveMessage = null;
	receiveMessage = receiveMessage(getUtilSocket(), timeout);
	if (receiveMessage == null)
	    return false;
	return receiveMessage.getExtension(SimpleExchange.simpleExchange).getResponse().getResponseType().equals(ResponseType.PROBE);
    }
}