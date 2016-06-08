package hooks;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import multiplayergamelauncher.ApplicationManager;
import net.Config;
import net.client.ClientThread;
import net.client.MembershipRequestDeniedException;
import net.server.ServerThread;

public class GameHooks {
	public static Set<ClientCreatedHook> onClientCreationHooks;
	public static Set<ServerCreatedHook> onServerCreationHooks;

	public static void addHook(ClientCreatedHook hook) {
		if (onClientCreationHooks == null) {
			onClientCreationHooks = new HashSet<>();
		}
		onClientCreationHooks.add(hook);
	}

	public static void addHook(ServerCreatedHook hook) {
		if (onServerCreationHooks == null) {
			onServerCreationHooks = new HashSet<>();
		}
		onServerCreationHooks.add(hook);
	}

	public static ClientThread createClient(final String address, int port, String username, final ApplicationManager listener) {
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
		for(ClientCreatedHook hook:onClientCreationHooks){
			hook.clientCreated(client);
		}
		return client;
	}

	public static ServerThread createServer(String name, int port, int players) {
		ServerThread server = new ServerThread(name, Config.PORT, players);
		server.start();
		for(ServerCreatedHook hook:onServerCreationHooks){
			hook.serverCreated(server);
		}
		return server;
	}
}
