package multiplayergamelauncher;

public abstract class Launcher {
	private ApplicationManager application;
	public ApplicationManager getApplication() {
		return application;
	}
	public void launch(){
		addHooks();
		application=new ApplicationManager();
		application.go();
	}
	/**
	 * add any gameHooks needed for this game
	 */
	public abstract void addHooks();
}
