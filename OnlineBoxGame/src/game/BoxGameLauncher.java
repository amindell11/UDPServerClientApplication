package game;
import hooks.ClientCreatedHook;
import hooks.GameHooks;
import multiplayergamelauncher.ApplicationLauncher;
import net.client.ClientThread;

public class BoxGameLauncher extends ApplicationLauncher{

	@Override
	public void addHooks() {
		super.addHooks();
		GameHooks.addHook(new ClientCreatedHook(){

			@Override
			public void clientCreated(ClientThread client) {
				if(client.isActiveMember()){
					GameClientThread gameClient=new GameClientThread();
					gameClient.getGame().setUsername(client.getUsername());
					gameClient.start();
				}
			}
			
		});
	}
	public static void main(String[] args){
		new BoxGameLauncher().launch();
	}
}
