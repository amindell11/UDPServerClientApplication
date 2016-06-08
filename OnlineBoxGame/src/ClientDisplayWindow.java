import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class ClientDisplayWindow extends Thread {
	GameManager game;
	@Override
	public void run() {
		try {
			AppGameContainer app = new AppGameContainer(new GameManager());
			app.setDisplayMode(500, 400, false);
			app.setAlwaysRender(true);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		new ClientDisplayWindow().run();
	}
	public GameManager getGame(){
		return game;
	}
}
