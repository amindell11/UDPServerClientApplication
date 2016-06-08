import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
/**
 * @author Ari
 */
public class GameManager extends BasicGame
{
	String username;
    public GameManager()
    {
        super("Box game");
        this.username="";
    }

    @Override
    public void init(GameContainer container) throws SlickException
    {
    }
 
    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {
    }
 
    public void render(GameContainer container, Graphics g) throws SlickException
    {
    	g.drawString(username, 10, 30);
    }
    public void setUsername(String username){
    	this.username=username;
    }
}