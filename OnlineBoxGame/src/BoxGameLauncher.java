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
					ClientDisplayWindow window=new ClientDisplayWindow();
					window.getGame().setUsername(client.getUsername());
					window.start();
				}
			}
			
		});
	}
	public static void main(String[] args){
		new BoxGameLauncher().launch();
	}
}
