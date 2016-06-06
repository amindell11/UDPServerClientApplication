package net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;
import net.connectionutil.ConnectionUtil;

public class ClientThread extends Thread {
	String username;
	int id;
	String address;
	private boolean activeMember;

	public ClientThread(String username, String address) {
		this.username = username;
		activeMember = false;
		this.address = address;
	}

	public String requestClusterMembership(String serverAddress, int port) throws IOException {
		ConnectionUtil.getUtilSocket().send(new SimpleExchangePacket(RequestType.CLUSTER_MEMBERSHIP_REQUEST, username).getPacket(serverAddress,
				port));
		DatagramPacket responsePacket = ConnectionUtil.receivePacket(Config.DEFAULT_TIMEOUT);
		if (responsePacket == null) {
			return "Packet timed out. Server unavailable";
		}
		simpleExchange responseMsg = new SimpleExchangePacket(responsePacket.getData()).getMessage();
		simpleExchangeResponse response = responseMsg.getResponse();
		if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_ACCEPT)) {
			int id = responseMsg.getId();
			this.id = id;
			activeMember=true;
			return null;
		} else if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_DENIED)) {
			return response.getResponseNote();
		}
		return "Unknown error.";
	}
	public boolean isActiveMember(){
		return activeMember;
	}
}
