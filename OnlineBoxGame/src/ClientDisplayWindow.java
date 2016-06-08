import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class ClientDisplayWindow extends Thread {
	@Override
	public void run() {
		try {
			AppGameContainer app = new AppGameContainer(new GameManager());
			app.setDisplayMode(500, 400, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
