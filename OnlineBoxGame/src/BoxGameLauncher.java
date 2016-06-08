import hooks.ClientCreatedHook;
import hooks.GameHooks;
import multiplayergamelauncher.ApplicationLauncher;
import net.client.ClientThread;

public class BoxGameLauncher extends ApplicationLauncher{

	@Override
	public void addHooks() {
		GameHooks.addHook(new ClientCreatedHook(){

			@Override
			public void clientCreated(ClientThread client) {
				new ClientDisplayWindow().run();
			}
			
		});
	}
	public static void main(String[] args){
		new BoxGameLauncher().launch();
	}
}
