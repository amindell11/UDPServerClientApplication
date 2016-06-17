package game;

import hooks.UnhandledMessageHook;
import net.client.ClientThread;
import net.proto.ExchangeProto.Exchange;

public class GameClientThread extends Thread {
	GameManager game;

	public GameManager getGame() {
		return game;
	}

	public GameClientThread(ClientThread client) {
		game = new GameManager();
		client.getHookManager().addHook(new UnhandledMessageHook(){

			@Override
			public void handleMessage(Exchange message) {
				System.out.println("hessaesda");
			}
			
		});
	}
	@Override
	public void run() {
		new ClientDisplayWindow(game).start();
		while (true) {
			synchronized (game.objects) {
			}
		}
	}

	public static void main(String[] args) {
		new GameClientThread(new ClientThread(null, null)).start();
	}
}
