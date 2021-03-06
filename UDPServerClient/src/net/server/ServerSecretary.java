package net.server;

import java.io.IOException;
import java.net.DatagramPacket;

import com.google.gson.Gson;

import net.Config;
import net.connectionutil.ConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse.ResponseType;

public class ServerSecretary {
    ServerThread parent;

    public ServerSecretary(ServerThread parent) {
	this.parent = parent;
    }

    protected void handleMessage(Exchange message, DatagramPacket packet) throws IOException {
	SimpleExchangeRequest req=message.getExtension(SimpleExchange.simpleExchange).getRequest();
	String address = packet.getAddress().getHostAddress();
	int port = packet.getPort();
	switch (req.getRequestType()) {
	case PROBE:
	    ConnectionUtil.sendResponse(ResponseType.PROBE, "", 0, parent.socket, address, port);
	    break;
	case SERVER_NAME:
	    ConnectionUtil.sendResponse(ResponseType.SERVER_NAME, parent.info.name, 0, parent.socket, address, port);
	    break;

	case SERVER_INFO:
	    String msg = new Gson().toJson(parent.info);
	    ConnectionUtil.sendResponse(ResponseType.SERVER_INFO, msg, 0, parent.socket, address, port);
	    break;
	case CLUSTER_MEMBERSHIP_REQUEST:
	    handleClusterMembershipRequest(req, packet);
	    break;
	default:
	    break;
	}
    }

    public void handleClusterMembershipRequest(SimpleExchangeRequest req, DatagramPacket packet) throws IOException {
	String address = packet.getAddress().getHostAddress();
	ResponseType decision;
	boolean validClient = true;
	String note = "";
	Client proposedClient = new Client(req.getRequestNote(), Client.assignNewId(), address, parent, packet.getPort());
	for (Client client : parent.clients.values()) {
	    if (Config.REQUIRE_UNIQUE_CLIENTS) {
		if (client.getAddress().equals(proposedClient.getAddress())) {
		    validClient = false;
		    note = "Active client at address " + proposedClient.getAddress() + " already in use. Close any instances and retry.";
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
	} else if (parent.info.hasPassword() && !req.getRequestNote().equals(parent.info.getPassword())) {
	    validClient = false;
	    note = "Incorrect server password.";
	}

	if (validClient) {
	    decision = ResponseType.CLUSTER_MEMBERSHIP_ACCEPT;
	    parent.clients.put(proposedClient.getClientId(), proposedClient);
	    proposedClient.start();
	} else {
	    Client.usedIds.remove(Integer.valueOf(proposedClient.getClientId()));
	    decision = ResponseType.CLUSTER_MEMBERSHIP_DENIED;
	}
	ConnectionUtil.sendResponse(decision, note, proposedClient.getClientId(), parent.socket, address, packet.getPort());

    }

}
