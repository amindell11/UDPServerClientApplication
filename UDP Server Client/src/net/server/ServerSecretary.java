package net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

import com.google.gson.Gson;

public class ServerSecretary {
	ServerThread parent;

	public ServerSecretary(ServerThread parent) {
		this.parent = parent;
	}

	protected void handleRequest(simpleExchangeRequest req,
			DatagramPacket packet) throws IOException {
		InetAddress address = packet.getAddress();

		switch (req.getRequestType()) {
		case PROBE:
			parent.socket.send(new SimpleExchangePacket(ResponseType.PROBE, "").getPacket(address, packet.getPort()));
			System.out.println(getClass().getName() + ">>>Sent packet to: " + packet.getAddress().getHostAddress());
			break;

		case SERVER_NAME:
			parent.socket.send(new SimpleExchangePacket(ResponseType.SERVER_NAME, parent.info.name).getPacket(address, packet.getPort()));
			break;

		case SERVER_INFO:
			String msg = new Gson().toJson(parent.info);
			parent.socket.send(new SimpleExchangePacket(ResponseType.SERVER_INFO, msg).getPacket(address,packet.getPort()));
			break;

		case CLUSTER_MEMBERSHIP_REQUEST:
			handleClusterMembershipRequest(req, packet);
			break;
		}
	}

	public void handleClusterMembershipRequest(simpleExchangeRequest req,
			DatagramPacket packet) throws IOException {
		InetAddress address = packet.getAddress();
		ResponseType decision;
		boolean validClient = true;
		String note = "";
		Client proposedClient = new Client(req.getRequestNote(),
				Client.assignNewId(), packet.getAddress().getHostAddress(), parent,packet.getPort());
		for (Client client : parent.clients.values()) {
			if (Config.REQUIRE_UNIQUE_CLIENTS) {
				if (client.getAddress().equals(proposedClient.getAddress())) {
					validClient = false;
					note = "Active client at address " + proposedClient.getAddress()
							+ " already in use. Close any instances and retry.";
					break;
				} else if (client.getUsername().equals(proposedClient.getUsername())) {
					validClient = false;
					note = "Invalid or taken username. Change username and retry.";
					break;
				}
			} else if (client.getUsername().equals(proposedClient.getUsername())) {
				validClient = false;
				note = "Invalid or taken username. Change username and retry.";
				break;
			}
		}
		// check if server is full
		if (parent.clients.size() == parent.info.getMaxClients()) {
			validClient = false;
			note = "Server already has the maximum number of connected clients.";
		} else if (parent.info.hasPassword()
				&& !req.getRequestNote().equals(parent.info.getPassword())) {
			validClient = false;
			note = "Incorrect server password.";
		}

		if (validClient) {
			decision = ResponseType.CLUSTER_MEMBERSHIP_ACCEPT;
			parent.clients.put(proposedClient.getClientId(), proposedClient);
			System.out.println("accepted");
			proposedClient.start();
		} else {
			decision = ResponseType.CLUSTER_MEMBERSHIP_DENIED;
		}
		System.out.println("sending packet back");
		System.out.println(address+" " +packet.getPort());
		parent.socket.send(new SimpleExchangePacket(decision, note,
				proposedClient.getClientId()).getPacket(address, packet.getPort()));
	}

}
