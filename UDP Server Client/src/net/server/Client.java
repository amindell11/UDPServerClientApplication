package net.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.connectionutil.ConnectionUtil;

public class Client extends Thread {
	public static List<Integer> usedIds;
	private String username;
	private int id;
	private String address;
	int port;
	private long lastCommTimestamp;
	private boolean open;
	private ServerThread parent;
	private long lastSentPingTimestamp;

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

	public void handleMessage(simpleExchange msg) {
		lastCommTimestamp = System.currentTimeMillis();
	}

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

	@Override
	public void run() {
		while (open) {
			open = isClientStillActive();
		}
		System.out.println("Client "+id+" closed.");
	}

	public boolean isClientStillActive() {
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
				parent.socket.send(new SimpleExchangePacket(RequestType.CLIENT_PING, "").getPacket(address, port));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;

	}

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
