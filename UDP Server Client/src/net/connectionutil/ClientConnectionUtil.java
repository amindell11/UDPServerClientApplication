package net.connectionutil;

import java.io.IOException;
import java.net.DatagramPacket;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

public class ClientConnectionUtil extends ConnectionUtil {
	public String requestClusterMembership(String serverAddress, int port, String username) throws IOException {
		getUtilSocket().send(new SimpleExchangePacket(RequestType.CLUSTER_MEMBERSHIP_REQUEST, username)
				.getPacket(serverAddress, port));
		DatagramPacket responsePacket = receivePacket(Config.DEFAULT_TIMEOUT);
		if(responsePacket==null)return "Packet timed out. Server unavailable";
		simpleExchangeResponse response = new SimpleExchangePacket(responsePacket.getData()).getResponse();
		if(response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_ACCEPT)){
			return null;
		}else if(response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_DENIED)){
			return response.getResponseNote();
		}else{
			return "Unknown error.";
		}
	}
}
