package multiplayergamelauncher;

import javax.swing.JOptionPane;

import net.Config;
import net.client.ClientThread;
import net.client.MembershipRequestDeniedException;
import net.server.ServerThread;

public class GameHooks {
	public static ClientThread createClient(final String address, int port, String username,
			final ApplicationManager listener) {
		final ClientThread client = new ClientThread(username, address);
		String errorMessage = "Unkown server error. Please try again later.";
		try {
			client.requestClusterMembership(address, port);
		} catch (MembershipRequestDeniedException e) {
			errorMessage = e.getMessage();
		}
		if (client.isActiveMember()) {
			client.start();
		} else {
			listener.showMessageDialog(errorMessage, "Error: Failed to connect", JOptionPane.ERROR_MESSAGE);
		}
		return client;
	}

	public static
	ServerThread createServer(String name,int port,int players) {
		ServerThread server = new ServerThread(name,Config.PORT,players);
		server.start();
		return server;
	}
}
