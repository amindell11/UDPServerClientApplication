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
		game.objects.add(new GameObject(0, 0, 0, 0));
		while (true) {
			synchronized (game.objects) {
				game.objects.remove(0);
				game.objects.add(new GameObject(0, 0, 0, 0));
			}
		}
	}

	public static void main(String[] args) {
		new GameClientThread().start();
	}
}
