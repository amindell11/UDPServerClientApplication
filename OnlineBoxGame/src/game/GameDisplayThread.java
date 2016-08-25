package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class GameDisplayThread<T extends GameManager> extends Thread {
	Class<T> type;
	T game;

	public GameDisplayThread(Class<T> t) {
		this.type=t;
		try {
			game=type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		initWindow();
	}

	public T getGame() {
		return game;
	}

	public void initWindow() {
		try {
			AppGameContainer app = new AppGameContainer(game);
			app.setDisplayMode(500, 400, false);
			app.setAlwaysRender(true);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
