package game;

import game_object.GameObject;

public class GameClientThread extends Thread {
	GameManager game;

	public GameManager getGame() {
		return game;
	}

	public GameClientThread() {
		game = new GameManager();
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
		new GameClientThread().start();
	}
}
