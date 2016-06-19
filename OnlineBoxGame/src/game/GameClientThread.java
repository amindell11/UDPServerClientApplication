package game;

import hooks.UnhandledMessageHook;
import net.client.ClientThread;
import net.proto.ExchangeProto.Exchange;

public class GameClientThread extends Thread implements UnhandledMessageHook {
	PlayerGameManager game;
	static int clientSendRate = 1;
	ClientThread client;

	public PlayerGameManager getGame() {
		return game;
	}

	public GameClientThread(ClientThread client) {
		game = new PlayerGameManager();
		client.getHookManager().addHook(this);
		this.client = client;
	}

	@Override
	public void run() {
		new GameDisplayThread(game).start();

		long startTime = System.currentTimeMillis();
		while (client.isActiveMember()) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > 1000 / clientSendRate) {
				startTime = currentTime;
				synchronized (game.objects) {
					update();
				}
			}

		}
	}

	public void update() {
	}

	public static void main(String[] args) {
		new GameClientThread(new ClientThread(null, null)).start();
	}

	@Override
	public void handleMessage(Exchange message) {

	}
}
