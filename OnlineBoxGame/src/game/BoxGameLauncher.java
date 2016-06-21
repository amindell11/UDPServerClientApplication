package game;
import hooks.ClientCreatedHook;
import hooks.GameHooks;
import hooks.ServerCreatedHook;
import multiplayergamelauncher.ApplicationLauncher;
import net.client.ClientThread;
import net.server.ServerThread;
import proto.GameStateExchangeProto.GameStateExchange;

public class BoxGameLauncher extends ApplicationLauncher{

    @Override
    public void addHooks() {
	super.addHooks();
	GameHooks.addHook(new ClientCreatedHook(){

	    @Override
	    public void clientCreated(ClientThread client) {
		client.addKnownMessageType(GameStateExchange.gameUpdate);
		if(client.isActiveMember()){
		    GameClientThread gameClient=new GameClientThread(client);
		    gameClient.getGame().setUsername(client.getUsername());
		    gameClient.start();
		}
	    }

	});
	GameHooks.addHook(new ServerCreatedHook(){

	    @Override
	    public void serverCreated(ServerThread server) {
		server.addKnownMessageType(GameStateExchange.gameUpdate);
		new GameServerThread(server).start();
	    }

	});
    }
    public static void main(String[] args){
	new BoxGameLauncher().launch();
    }
}
