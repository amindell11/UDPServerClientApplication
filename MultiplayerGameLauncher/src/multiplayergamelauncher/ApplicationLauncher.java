package multiplayergamelauncher;

public abstract class ApplicationLauncher {
	public void launch(){
		addHooks();
		new ApplicationManager().go();
	}
	/**
	 * add any gameHooks needed for this game
	 */
	public abstract void addHooks();
}
