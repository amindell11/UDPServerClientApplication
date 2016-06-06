package net.connectionutil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import net.Config;
import net.SimpleExchangePacket;
import net.client.ClientThread;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

public class ClientConnectionUtil extends ConnectionUtil {
	public static ClientThread requestClusterMembership(String serverAddress, int port, String username)
			throws IOException {
		getUtilSocket().send(new SimpleExchangePacket(RequestType.CLUSTER_MEMBERSHIP_REQUEST, username)
				.getPacket(serverAddress, port));
		DatagramPacket responsePacket = receivePacket(Config.DEFAULT_TIMEOUT);
		if (responsePacket == null){
			System.out.println("Packet timed out. Server unavailable");
			return null;
		}
		simpleExchange responseMsg = new SimpleExchangePacket(responsePacket.getData()).getMessage();
		simpleExchangeResponse response = responseMsg.getResponse();
		if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_ACCEPT)) {
			int id = responseMsg.getId();
			return new ClientThread(username, id, InetAddress.getLocalHost().getHostAddress());
		} else if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_DENIED)) {
			System.out.println(response.getResponseNote());
			return null;
		} else {
			System.out.println("Unknown error.");
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println(requestClusterMembership("127.0.0.1", Config.PORT, "hi"));
	}
}
