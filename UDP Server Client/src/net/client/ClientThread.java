package net.client;

public class ClientThread extends Thread {
	String username;
	int id;
	String address;
	public ClientThread(String username,int id,String address){
		this.username=username;
		this.id=id;
		this.address=address;
	}
}
