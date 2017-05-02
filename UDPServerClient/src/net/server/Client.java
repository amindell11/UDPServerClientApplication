package net.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.Config;
import net.connectionutil.ConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest.RequestType;

/**
 * Code to handle client's on the server. The server's representation of a
 * client. It is important to note that this is all serverside.
 */
public class Client extends Thread {

    /**
     * Ids already used to enumerate other clients, whether those clients are
     * still alive or dead.
     */
    public static List<Integer> usedIds;

    private final String username;
    private final int id;
    private final String address;
    private final int port;

    public int getPort() {
	return port;
    }

    private final ServerThread parent;

    private long lastCommTimestamp;
    private boolean open;
    private long lastSentPingTimestamp;

    /**
     * Creates a single client object to represent one of the client's on the
     * server
     * 
     * @param username
     *            The client's chosen name
     * @param id
     *            The server assigned id of the client
     * @param address
     *            The IP address of the client's computer, represented as a
     *            string
     * @param parent
     *            The object running the server
     * @param port
     *            The port on which the client's computer is communicating with
     *            the server
     */
    public Client(String username, int id, String address, ServerThread parent, int port) {
	this.username = username;
	this.id = id;
	this.address = address;
	this.parent = parent;
	this.port = port;
	open = true;
	lastCommTimestamp = System.currentTimeMillis();
	lastSentPingTimestamp = 0;
    }

    /**
     * Handles a message from the client represented by this object
     * 
     * @param msg
     *            The simple exchange message the client sent
     */
    public void handleMessage(Exchange msg) {
	lastCommTimestamp = System.currentTimeMillis();
    }

    /**
     * Assigns a unique new id for a client. Note: Ids of dead clients are not
     * deleted from the used pile.
     * 
     * @return The new unique id
     */
    public static int assignNewId() {
	if (usedIds == null) {
	    usedIds = new ArrayList<>();
	}
	int generatedId = 1;
	while (usedIds.contains(generatedId)) {
	    generatedId++;
	}
	usedIds.add(generatedId);
	return generatedId;
    }

    /**
     * Checks to make sure the client is still active, if it isn't deletes this
     * object.
     */
    @Override
    public void run() {
	long timeSinceLastComm = 0;
	while (parent.isOpen() && open) {   
	   
	    System.out.println(timeSinceLastComm);
	    do{ 
		try {
		    Thread.sleep(Config.PingSettings.CLIENT_MAX_DEAD_TIME - timeSinceLastComm);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		timeSinceLastComm = System.currentTimeMillis() - lastCommTimestamp;
	    }
	    while(timeSinceLastComm < Config.PingSettings.CLIENT_MAX_DEAD_TIME);
	    
	    open = isClientStillActive();
	    timeSinceLastComm = System.currentTimeMillis() - lastCommTimestamp;

	}
	if (parent.isOpen()) {
	    parent.killClient(id);
	    System.out.println("Client " + id + " closed.");
	} else {
	    System.out.println("client closed with server");
	}
    }
    
    

    /**
     * Sends messages to the client represented by this object to check if it is
     * still connected to the server.
     * 
     * @return False if the client has timed out, True if the client is still on
     **/
    private boolean isClientStillActive() {
	long pingStart = System.currentTimeMillis();
	for(int i = 0; i < Config.PingSettings.NUMBER_OF_PINGS; i++){
	    try {
		ConnectionUtil.sendRequest(RequestType.CLIENT_PING, "", 0, parent.socket, address, port);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    try {
		Thread.sleep(Config.PingSettings.TIME_BETWEEN_PINGS);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	try {
	    Thread.sleep(Config.DEFAULT_TIMEOUT);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return lastCommTimestamp > pingStart;
    }

    /**
     * End's this object's thread
     */
    public void close() {
	open = false;
    }

    public String getAddress() {
	return address;
    }

    public String getUsername() {
	return username;
    }

    public int getClientId() {
	return id;
    }
}