import multiplayergamelauncher.ApplicationManager;
import multiplayergamelauncher.GameHooks;
import net.client.ClientThread;
import net.server.ServerThread;

public class BoxGameHooks extends GameHooks {
	public static ClientThread createClient(final String address, int port, String username, final ApplicationManager listener) {
		ClientThread client=GameHooks.createClient(address, port, username, listener);
		new ClientDisplayWindow().start();
		return client;
	}
}
