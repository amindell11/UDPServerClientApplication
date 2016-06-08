package net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;
import net.connectionutil.ConnectionUtil;

public class ClientThread extends Thread {
	String username;
	int id;
	String address;
	// TODO switch these to a serverInfo object
	DatagramSocket socket;
	String serverAddress;
	public String getServerAddress() {
	    return serverAddress;
	}
	int serverPort;
	private boolean activeMember;
	private boolean open;

	public ClientThread(String username, String address) {
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.username = username;
		activeMember = false;
		open = false;
		this.address = address;
	}
	public void close(){
	    open=false;
	    socket.close();
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
		DatagramPacket receive = ConnectionUtil.receivePacket(socket);
		handlePacket(receive);
	}

	public void handlePacket(DatagramPacket packet) {
		simpleExchange message = new SimpleExchangePacket(packet.getData()).getMessage();
		System.out.println(message);
		if (message.hasRequest() && message.getRequest().getRequestType().equals(RequestType.CLIENT_PING)) {
			try {
				System.out.println("ping recieved from server. responding");
				ConnectionUtil.sendMessage(new SimpleExchangePacket(ResponseType.CLIENT_PING, "",id).getMessage(), socket, serverAddress, serverPort);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean requestClusterMembership(String serverAddress, int port) throws MembershipRequestDeniedException {
		DatagramPacket responsePacket = null;
		try {
			ConnectionUtil.sendMessage(new SimpleExchangePacket(RequestType.CLUSTER_MEMBERSHIP_REQUEST, username).getMessage(), socket, serverAddress, port);
			responsePacket = ConnectionUtil.receivePacket(socket, Config.DEFAULT_TIMEOUT);
		} catch (IOException e) {

		}
		if (responsePacket == null) {
			throw new MembershipRequestDeniedException("Packet timed out. Server unavailable");
		}

		simpleExchange responseMsg = new SimpleExchangePacket(responsePacket.getData()).getMessage();
		simpleExchangeResponse response = responseMsg.getResponse();
		if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_ACCEPT)) {
			int id = responseMsg.getId();
			this.id = id;
			this.serverAddress = serverAddress;
			this.serverPort = port;
			activeMember = true;
		} else if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_DENIED)) {
			throw new MembershipRequestDeniedException(response.getResponseNote());
		}
		return activeMember;
	}

	public boolean isActiveMember() {
		return activeMember;
	}
	public static void main(String[] args) throws UnknownHostException{
		String username="  ";
		String address=InetAddress.getLocalHost().getHostAddress();
		final ClientThread client = new ClientThread(username, address);
		String errorMessage = "Unkown server error. Please try again later.";
		try {
			client.requestClusterMembership(address, Config.PORT);
		} catch (MembershipRequestDeniedException e) {
			errorMessage=e.getMessage();
		}
		if (client.isActiveMember()) {
			client.start();
		} else {
			System.out.println(errorMessage);
		}
	}
}
