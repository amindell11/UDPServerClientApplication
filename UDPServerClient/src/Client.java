import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import net.connectionutil.ConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest.RequestType;

public class Client {
    public static void main(String[] args) throws SocketException, IOException {
	ConnectionUtil.sendRequest(RequestType.PROBE,"",0, new DatagramSocket(), "127.0.0.1", 8888);
    }
}