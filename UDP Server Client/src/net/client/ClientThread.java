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
	private boolean open;

	public ClientThread(String username, String address) {
		this.username = username;
		activeMember = false;
		open = false;
		this.address = address;
	}

	@Override
	public void run() {
		if (!activeMember) {
			System.out.println("Error: Client not connected to a server. Cannot begin update cycle.");
			return;
		}
		open = true;
		while (open) {
			try {
				update();
			} catch (IOException e) {
				System.out.println("update tripped. attempting to resume.");
			}
		}
	}

	public void update() throws IOException {
		DatagramPacket receive=ConnectionUtil.receivePacket();
		handlePacket(receive);
	}
	public void handlePacket(DatagramPacket packet){
		//TODO
	}
	public boolean requestClusterMembership(String serverAddress, int port) throws MembershipRequestDeniedException {
		DatagramPacket responsePacket=null;
		try{
		ConnectionUtil.getUtilSocket().send(new SimpleExchangePacket(RequestType.CLUSTER_MEMBERSHIP_REQUEST, username)
				.getPacket(serverAddress, port));
		responsePacket = ConnectionUtil.receivePacket(Config.DEFAULT_TIMEOUT);
		}catch(IOException e){
			
		}
		if (responsePacket == null) {
			throw new MembershipRequestDeniedException("Packet timed out. Server unavailable");
		}
		
		simpleExchange responseMsg = new SimpleExchangePacket(responsePacket.getData()).getMessage();
		simpleExchangeResponse response = responseMsg.getResponse();
		if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_ACCEPT)) {
			int id = responseMsg.getId();
			this.id = id;
			activeMember = true;
		} else if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_DENIED)) {
			throw new MembershipRequestDeniedException(response.getResponseNote());
		}
		return activeMember;
	}

	public boolean isActiveMember() {
		return activeMember;
	}
}
