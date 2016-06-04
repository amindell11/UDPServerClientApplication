package net.server;

public final class ServerInfo {
	String address;
	String name;
	int numClients;
	int maxClients;
	int port;
	public ServerInfo(String address,int port,String name, int numClients, int maxClients){
		this.address=address;
		this.port=port;
		this.name=name;
		this.numClients=numClients;
		this.maxClients=maxClients;
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
}