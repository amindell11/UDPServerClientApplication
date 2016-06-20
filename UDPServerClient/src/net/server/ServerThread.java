package net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Extension;
import com.google.protobuf.ExtensionRegistry;

import hooks.HookManager;
import net.Config;
import net.connectionutil.ConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;

public class ServerThread extends Thread {
    private HookManager hookManager;
    private ExtensionRegistry knownMessageTypes;

    protected ServerInfo info;
    private ServerSecretary secretary;
    protected DatagramSocket socket;
    protected Map<Integer, Client> clients;
    private boolean open;

    public ServerInfo getInfo() {
	return info;
    }

    public ServerThread(String name, int port) {
	this(name, port, Config.MAX_CLIENTS);
    }

    public ServerThread(String name, int port, int maxClients) {
	try {
	    info = new ServerInfo(InetAddress.getLocalHost().getHostAddress(), port, name, 0, maxClients);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
	secretary = new ServerSecretary(this);
	hookManager = new HookManager();
	knownMessageTypes = ExtensionRegistry.newInstance();
	addKnownMessageType(SimpleExchange.simpleExchange);

    }

    /**
     * Removes all references to and kills a client.
     * 
     * @param id
     *            Server assigned id of the client to close
     */
    protected void killClient(int id) {
	if (clients.containsKey(id)) {
	    clients.remove(id).close();
	    Client.usedIds.remove(Integer.valueOf(id));
	}
    }

    public void announceToClients(Exchange message) throws IOException {
	for (int id : clients.keySet()) {
	    sendMessage(message, id);
	}
    }

    public void sendMessage(Exchange message, int clientId) throws IOException {
	Client recipient = clients.get(clientId);
	ConnectionUtil.sendMessage(message, socket, recipient.getAddress(), recipient.getPort());
    }

    public void init() {
	try {
	    socket = new DatagramSocket(info.port, InetAddress.getByName("0.0.0.0"));
	    socket.setBroadcast(true);
	    clients = new HashMap<Integer, Client>();
	    System.out.println("server running at " + InetAddress.getLocalHost() + " port " + info.port);
	} catch (SocketException e) {
	    e.printStackTrace();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
	open = true;
    }

    public ServerThread(int port) {
	this("default", port);
    }

    public boolean isOpen() {
	return open;
    }

    public void update() {

	// Receive a packet
	DatagramPacket packet = null;
	try {
	    packet = ConnectionUtil.receivePacket(socket);
	} catch( SocketException e){
	    return;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println("packet recived");
	Exchange exchange = ConnectionUtil.convertMessage(packet.getData(), knownMessageTypes);
	// Handles simpleExchanges, otherwise sends it to UnhandledMessageHook
	if (exchange.hasExtension(SimpleExchange.simpleExchange)) {
	    SimpleExchange msg = exchange.getExtension(SimpleExchange.simpleExchange);
	    // Packet received
	    System.out.println(msg);

	    if (exchange.hasId() && exchange.getId() != 0) {
		System.out.println(exchange.getId());
		System.out.println("message intended for client " + exchange.getId() + ": forwarding packet to client handle method");
		clients.get(exchange.getId()).handleMessage(exchange);
	    } else {
		System.out.println("asking secretary to handle it");
		try {
		    secretary.handleMessage(exchange, packet);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    info.numClients = clients.size();
	} else {
	    hookManager.handleMessage(exchange);
	}

    }

    public void addKnownMessageType(Extension<?, ?> extension) {
	knownMessageTypes.add(extension);
    }

    @Override
    public void run() {
	init();
	while (open) {
	    update();
	}
    }

    public void close() {
	open = false;
	socket.close();
    }

    public static void main(String[] args) {
	new ServerThread("test", Config.PORT).start();
    }

    public HookManager getHookManager() {
	return hookManager;
    }

    public Client[] getClients() {
	if (clients != null) {
	    return (Client[]) clients.values().toArray();
	} else {
	    return new Client[0];
	}
    }

}
