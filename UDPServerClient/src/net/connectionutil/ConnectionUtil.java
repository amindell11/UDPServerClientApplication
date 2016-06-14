package net.connectionutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.google.protobuf.ExtensionRegistry;

import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest.RequestType;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse.ResponseType;

public class ConnectionUtil {
    private static DatagramSocket socket;

    public static Exchange receiveMessage(DatagramSocket socket) throws IOException {
	DatagramPacket receivePacket = receivePacket(socket);
	Exchange message = convertMessage(receivePacket.getData());
	return message;
    }

    public static Exchange convertMessage(byte[] data) {
	ExtensionRegistry registry = ExtensionRegistry.newInstance();
	registry.add(SimpleExchange.simpleExchange);
	ByteArrayInputStream aInput = new ByteArrayInputStream(data);
	Exchange comm = null;
	try {
	    comm = Exchange.parseDelimitedFrom(aInput, registry);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return comm;

    }

    public static Exchange receiveMessage(DatagramSocket socket, int timeout) {
	DatagramPacket receivePacket = receivePacket(socket, timeout);
	Exchange message = convertMessage(receivePacket.getData());
	return message;
    }

    public static DatagramPacket receivePacket(DatagramSocket socket) throws IOException {
	byte[] recvBuf = new byte[15000];
	DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
	    socket.receive(receivePacket);
	return receivePacket;
    }

    public static DatagramPacket receivePacket(DatagramSocket socket, int timeout) {
	DatagramPacket receivePacket = null;
	try {
	    socket.setSoTimeout(timeout);
	    receivePacket = receivePacket(socket);
	    socket.setSoTimeout(0);
	} catch (SocketTimeoutException e) {
	    System.out.println("recieve timed out");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return receivePacket;
    }

    protected static DatagramSocket getUtilSocket() {
	if (socket == null) {
	    try {
		socket = new DatagramSocket();
	    } catch (SocketException e) {
		e.printStackTrace();
	    }
	}
	return socket;
    }

    private static void sendBytes(byte[] bytes, DatagramSocket sourceSocket, String address, int port) throws IOException {
	InetAddress netAddress = InetAddress.getByName(address);
	sourceSocket.send(new DatagramPacket(bytes, bytes.length, netAddress, port));
    }

    public static void sendMessage(Exchange message, DatagramSocket sourceSocket, String address, int port) throws IOException {
	ByteArrayOutputStream aOutput = new ByteArrayOutputStream(15000);
	message.writeDelimitedTo(aOutput);
	sendBytes(aOutput.toByteArray(), sourceSocket, address, port);
    }

    public static void sendRequest(RequestType req, String note, int id, DatagramSocket sourceSocket, String address, int port) throws IOException {
	Exchange message = buildSimpleExchangeRequest(req, note, id);
	sendMessage(message, sourceSocket, address, port);
    }

    public static void sendResponse(ResponseType resp, String note, int id, DatagramSocket sourceSocket, String address, int port) throws IOException {
	Exchange message = buildSimpleExchangeResponse(resp, note, id);
	sendMessage(message, sourceSocket, address, port);
    }

    public static Exchange buildSimpleExchangeRequest(RequestType request, String note, int id) {
	Exchange message = Exchange.newBuilder().setId(id).setExtension(SimpleExchange.simpleExchange, SimpleExchange.newBuilder().setRequest(SimpleExchangeRequest.newBuilder().setRequestType(request).setRequestNote(note).build()).build()).build();
	return message;
    }

    public static Exchange buildSimpleExchangeResponse(ResponseType response, String note, int id) {
	Exchange message = Exchange.newBuilder().setId(id).setExtension(SimpleExchange.simpleExchange, SimpleExchange.newBuilder().setResponse(SimpleExchangeResponse.newBuilder().setResponseType(response).setResponseNote(note).build()).build()).build();
	return message;
    }
}
