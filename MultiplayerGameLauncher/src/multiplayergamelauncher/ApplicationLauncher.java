package multiplayergamelauncher;

import java.net.InetAddress;
import java.net.UnknownHostException;

import hooks.ClientCreatedHook;
import hooks.GameHooks;
import hooks.ServerCreatedHook;
import net.client.ClientThread;
import net.server.ServerThread;

public class ApplicationLauncher extends Launcher {

	@Override
	public void addHooks() {
		GameHooks.addHook(new ClientCreatedHook() {

			@Override
			public void clientCreated(ClientThread client) {
				if (client.isActiveMember()) {
					try {
						if(client.getServerAddress().equals(InetAddress.getLocalHost().getHostAddress())){
							getApplication().serverConsolePanel.setClient(client);
						}else{
							getApplication().clientConsolePanel.onEnter(client);
							getApplication().progressTo(AppState.CLIENT_CONSOLE);
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			}

		});
		GameHooks.addHook(new ServerCreatedHook() {

			@Override
			public void serverCreated(ServerThread server) {
				getApplication().serverConsolePanel.onEnter(server);
		    	getApplication().progressTo(AppState.SERVER_CONSOLE);
			}

		});
	}

	public static void main(String[] args) {
		new ApplicationLauncher().launch();
	}

}
