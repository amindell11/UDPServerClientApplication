package net.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;

public class Client extends Thread{
    public static List<Integer> usedIds;
    private String username;
    private int id;
    private String address;
    private long lastCommTimestamp;
    private boolean open;
    private long lastSentPingTimestamp;
    private ServerThread parent;

    public Client(String username, int id, String address, ServerThread parent){
	this.username = username;
	this.id = id;
	this.address = address;
	this.parent = parent;
	open = true;
	lastCommTimestamp = System.currentTimeMillis();
	lastSentPingTimestamp = 0;
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
	    long currentTime = System.currentTimeMillis();
	    if(lastCommTimestamp - currentTime > 20000){
		break;
	    }
	    else if(lastCommTimestamp - currentTime > 10000 && lastSentPingTimestamp - currentTime > 1000){
		lastSentPingTimestamp = currentTime;
		System.out.println("New Ping");
		try{
		    parent.socket.send(new SimpleExchangePacket(RequestType.PROBE, "").getPacket(getAddress(), parent.info.getPort()));
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
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
