package game;

import java.awt.event.ActionEvent;

import hooks.ClientCreatedHook;
import hooks.GameHooks;
import hooks.ServerCreatedHook;
import multiplayergamelauncher.ApplicationLauncher;
import net.client.ClientThread;
import net.server.ServerThread;
import proto.GameStateExchangeProto.GameStateExchange;

public class BoxGameLauncher extends ApplicationLauncher {

	@Override
	public void addHooks() {
		super.addHooks();
		GameHooks.addHook(new ClientCreatedHook() {

			@Override
			public void clientCreated(ClientThread client) {
				client.addKnownMessageType(GameStateExchange.gameUpdate);
				if (client.isActiveMember()) {
					GameDisplayThread<PlayerGameManager> dispThread = new GameDisplayThread<>(PlayerGameManager.class);
					dispThread.game.setInitListener((ActionEvent e) -> {
						GameClientThread gameClient = new GameClientThread(client, dispThread.game);
						gameClient.getGame().setUsername(client.getUsername());
						gameClient.start();
					});
					dispThread.start();
				}
			}

		});
		GameHooks.addHook(new ServerCreatedHook() {

			@Override
			public void serverCreated(ServerThread server) {
				server.addKnownMessageType(GameStateExchange.gameUpdate);
				new GameServerThread(server).start();
			}

		});
	}

	public static void main(String[] args) {
		new BoxGameLauncher().launch();
	}
}
