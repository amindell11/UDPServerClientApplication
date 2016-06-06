package net.server;

public final class ServerInfo {
	String address;
	String name;
	int numClients;
	int maxClients;
	int port;
	String password;
	public ServerInfo(String address,int port,String name, int numClients, int maxClients){
		this.address=address;
		this.port=port;
		this.name=name;
		this.numClients=numClients;
		this.maxClients=maxClients;
	}
	public ServerInfo(String address,int port,String name, int numClients, int maxClients,String password){
		this.address=address;
		this.port=port;
		this.name=name;
		this.numClients=numClients;
		this.maxClients=maxClients;
		this.password=password;
	}
	public void setServerAddress(String address){
		this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServerName(){
		return name;
	}
	public int getNumClients(){
		return numClients;
	}
	public int getMaxClients(){
		return maxClients;
	}
	public boolean hasPassword(){
		return password!=null;
	}
	public String getPassword(){
		return password;
	}
}