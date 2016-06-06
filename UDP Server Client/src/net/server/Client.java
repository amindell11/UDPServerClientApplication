package net.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.connectionutil.ConnectionUtil;

public class Client extends Thread{
	public static List<Integer> usedIds;
	private String username;
	private int id;
	private String address;
	private long lastCommTimestamp;
	private boolean open;
	private ServerThread parent;

	public Client(String username, int id, String address, ServerThread parent){
		this.username = username;
		this.id = id;
		this.address = address;
		this.parent = parent;
		open = true;
		lastCommTimestamp = System.currentTimeMillis();
	}

	public void handleMessage(simpleExchange msg){
		lastCommTimestamp = System.currentTimeMillis();
	}

	public static int assignNewId(){
		if(usedIds == null){
			usedIds = new ArrayList<>();
		}
		int generatedId = 1;
		while(usedIds.contains(generatedId)){
			generatedId++;
		}
		usedIds.add(generatedId); 
		return generatedId;
	}

	@Override
	public void run(){
		while(open){
			open=isClientStillActive();
		}
		System.out.println("Client no longer active. Closing.");
	}
	public boolean isClientStillActive(){
		long currentTime = System.currentTimeMillis();
		long timeSinceCommed = currentTime-lastCommTimestamp;
		if(timeSinceCommed > Config.CLIENT_MAX_DEAD_TIME){
				try{
					parent.socket.send(new SimpleExchangePacket(RequestType.PROBE, "").getPacket(getAddress(), parent.info.getPort()));
					ConnectionUtil.receivePacket(Config.DEFAULT_TIMEOUT);
					lastCommTimestamp=currentTime;
					return true;
				} catch (IOException e) {
					return false;
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
	public int getClientId(){
		return id;
	}
}
