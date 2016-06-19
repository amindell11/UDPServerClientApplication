package game;

import hooks.UnhandledMessageHook;
import net.proto.ExchangeProto.Exchange;
import net.server.ServerThread;

/**
 * This class manages the game update messages sent from clients. It recieves messages from clients and decides what to send to other
 * clients as updates.
 * @author Josh
 *
 */
public class GameServerThread extends Thread implements UnhandledMessageHook{

    private ServerThread server;
    
    /**
     * @param server The object running the server thread
     */
    GameServerThread(ServerThread server){
	this.server = server;
	server.getHookManager().addHook(this);
	
    }

    public void update(){
    	
    }
    public void run(){
    	while(server.isOpen()){
    		update();
    	}
    }
    
    @Override
    public void handleMessage(Exchange message) {
	
    }

}
