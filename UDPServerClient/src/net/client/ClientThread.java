package net.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.google.protobuf.Extension;
import com.google.protobuf.ExtensionRegistry;

import hooks.HookManager;
import net.Config;
import net.connectionutil.ConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest.RequestType;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse.ResponseType;

public class ClientThread extends Thread {
    HookManager hookManager;
    ExtensionRegistry knownMessageTypes;
    
    String username;
    int id;
    String address;
    // TODO switch these to a serverInfo object
    DatagramSocket socket;
    String serverAddress;

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
	hookManager = new HookManager();
	knownMessageTypes=ExtensionRegistry.newInstance();
	addKnownMessageType(SimpleExchange.simpleExchange);
    }

    public String getUsername() {
	return username;
    }

    public void close() {
	open = false;
	socket.close();
    }

    public String getServerAddress() {
	return serverAddress;
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
	Exchange receive = ConnectionUtil.receiveMessage(socket,knownMessageTypes);
	handleMessage(receive);
    }

    public void handleMessage(Exchange exchange) {
	if(exchange.hasExtension(SimpleExchange.simpleExchange)){
	    SimpleExchange message = exchange.getExtension(SimpleExchange.simpleExchange);
	    System.out.println(message);
	    
	    //Responds to a ping from the server by sending a ping back
	    if (message.hasRequest() && message.getRequest().getRequestType().equals(RequestType.CLIENT_PING)) {
		try {
		    System.out.println("ping recieved from server. responding");
		    ConnectionUtil.sendResponse(ResponseType.CLIENT_PING, "", id, socket, serverAddress, serverPort);

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    else{
		hookManager.handleMessage(exchange);
	    }
	}
    }

    public boolean requestClusterMembership(String serverAddress, int port) throws MembershipRequestDeniedException {
	Exchange responseMessage = null;
	try {
	    ConnectionUtil.sendRequest(RequestType.CLUSTER_MEMBERSHIP_REQUEST, username,0, socket, serverAddress, port);
	    responseMessage = ConnectionUtil.receiveMessage(socket, Config.DEFAULT_TIMEOUT,knownMessageTypes);
	} catch (IOException e) {

	}
	if (responseMessage== null) {
	    throw new MembershipRequestDeniedException("Packet timed out. Server unavailable");
	}

	SimpleExchangeResponse response = responseMessage.getExtension(SimpleExchange.simpleExchange).getResponse();
	if (response.getResponseType().equals(ResponseType.CLUSTER_MEMBERSHIP_ACCEPT)) {
	    int id = responseMessage.getId();
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

    public HookManager getHookManager() {
	return hookManager;
    }
    public void addKnownMessageType(Extension<?,?> extension){
	knownMessageTypes.add(extension);
    }
    public static void main(String[] args) throws UnknownHostException {
	String username = "  ";
	String address = InetAddress.getLocalHost().getHostAddress();
	final ClientThread client = new ClientThread(username, address);
	String errorMessage = "Unkown server error. Please try again later.";
	try {
	    client.requestClusterMembership(address, Config.PORT);
	} catch (MembershipRequestDeniedException e) {
	    errorMessage = e.getMessage();
	}
	if (client.isActiveMember()) {
	    client.start();
	} else {
	    System.out.println(errorMessage);
	}
    }
}
