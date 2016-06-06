package net.server;

import java.util.ArrayList;
import java.util.List;

public class Client {
	public static List<Integer> usedIds;
	String username;
	int id;
	String address;
	long lastCommTimestamp;
	public Client(String username,int id,String address){
		this.username=username;
		this.id=id;
		this.address=address;
		lastCommTimestamp=System.currentTimeMillis();
	}
	
	public static int assignNewId() {
		if(usedIds==null)usedIds=new ArrayList<>();
		int generatedId=1;
		while(usedIds.contains(generatedId)){
			generatedId++;
		}
		usedIds.add(generatedId);
		return generatedId;
	}
}
