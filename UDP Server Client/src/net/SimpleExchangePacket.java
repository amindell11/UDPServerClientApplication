package net;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import net.communication.SimpleExchangeComm;
import net.communication.SimpleExchangeComm.simpleExchange;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

public class SimpleExchangePacket {
	simpleExchange.Builder builder;
	simpleExchange message;
	private SimpleExchangePacket(){
		builder = SimpleExchangeComm.simpleExchange.newBuilder();
	}
	public SimpleExchangePacket(RequestType requestType, String note) {
		this();
		builder.setRequest(simpleExchangeRequest.newBuilder()
				.setRequestType(requestType)
				.setRequestNote(note)
				);
		message=builder.build();
	}

	public SimpleExchangePacket(ResponseType responseType, String note) {
		this();
		builder.setResponse(simpleExchangeResponse.newBuilder()
				.setResponseType(responseType)
				.setResponseNote(note)
				);
		message=builder.build();
	}
	public SimpleExchangePacket(byte[] bytes){
		this();
    	ByteArrayInputStream aInput = new ByteArrayInputStream(bytes);
    	SimpleExchangeComm.simpleExchange comm=null;
		try {
			comm = SimpleExchangeComm.simpleExchange.parseDelimitedFrom(aInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	message=comm;

	}
	public byte[] getBytes() throws IOException{
		ByteArrayOutputStream aOutput = new ByteArrayOutputStream(15000);
		builder.build().writeDelimitedTo(aOutput);
		return aOutput.toByteArray();
	}
	public DatagramPacket getPacket(InetAddress address,int port){
		byte[] req={};
		try {
			req = getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DatagramPacket(req,req.length,address,port);
	}
	public boolean isResponse(){
		return message.hasResponse();
	}
	public boolean isRequest(){
		return message.hasRequest();
	}
	public simpleExchangeRequest getRequest(){
		if(isRequest()){
			return message.getRequest();
		}
		System.out.println("message does not have request");
		return null;
	}
	public simpleExchangeResponse getResponse(){
		if(isResponse()){
			return message.getResponse();
		}
		System.out.println("message does not have response");
		return null;
	}

}