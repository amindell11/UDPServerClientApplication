package net.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.connectionutil.ConnectionUtil;

/**
 * Code to handle client's on the server. The server's representation of a client.
 * It is important to note that this is all serverside.
 */
public class Client extends Thread {
    
    	/**
    	 * Ids already used to enumerate other clients, whether those clients are still alive or dead.
    	 */
	public static List<Integer> usedIds;
	
	private final String username;
	private final int id;
	private final String address;
	private final int port;
	private final ServerThread parent;
	
	private long lastCommTimestamp;
	private boolean open;
	private long lastSentPingTimestamp;

	/**
	 * Creates a single client object to represent one of the client's on the server
	 * @param username The client's chosen name
	 * @param id The server assigned id of the client
	 * @param address The IP address of the client's computer, represented as a string
	 * @param parent The object running the server
	 * @param port The port on which the client's computer is communicating with the server
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
	 * @param msg The simple exchange message the client sent 
	 */
	public void handleMessage(simpleExchange msg) {
		lastCommTimestamp = System.currentTimeMillis();
	}

	/**
	 * Assigns a unique new id for a client. Note: Ids of dead clients are not deleted from the used pile.
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
	 * Checks to make sure the client is still active, if it isn't deletes this object.
	 */
	@Override
	public void run() {
		while (open) {
			open = isClientStillActive();
		}
		parent.killClient(id);
		System.out.println("Client "+id+" closed.");
	}

	/**
	 * Sends messages to the client represented by this object to check if it is still connected to the server.
	 * @return False if the client has timed out, True if the client has not yet timed out. Note a true value can be returned
	 * even if the client has disconnected.
	 */
	private boolean isClientStillActive() {
		long currentTime = System.currentTimeMillis();
		long timeSinceLastComm = currentTime - lastCommTimestamp;

		// If this is true, kill the thread by returning false
		// The thread has been unresponsive for more than 20 seconds
		if (timeSinceLastComm > Config.PingSettings.TOTAL_WAIT) {
			return false;
		}

		// Ping the client if the time since last communication is more than 10
		// seconds
		// Also don't ping again if we already sent a ping less than a second
		// ago
		else if (timeSinceLastComm > Config.PingSettings.CLIENT_MAX_DEAD_TIME && currentTime - lastSentPingTimestamp > Config.PingSettings.TIME_BETWEEN_PINGS) {
			lastSentPingTimestamp = currentTime;
			try {
				ConnectionUtil.sendMessage(new SimpleExchangePacket(RequestType.CLIENT_PING, "").getMessage(), parent.socket, address, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;

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