package game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class ClientDisplayWindow extends Thread {
    GameManager game;
    public ClientDisplayWindow(GameManager game){
	this.game=game;
    }
    @Override
    public void run() {
	initWindow();
    }
    public void initWindow(){
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
    public GameManager getGame(){
	return game;
    }
}
