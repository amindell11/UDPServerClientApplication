package multiplayergamelauncher;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import panel.ClientConsolePanel;
import panel.DirectConnectPanel;
import panel.EditProfilePanel;
import panel.HomePanel;
import panel.ServerConsolePanel;
import panel.ServerCreationPanel;
import panel.serverselect.ServerSelectPanel;
import profile.User;
/**
 *
 * @author amind_000
 */
public class ApplicationManager implements Runnable, ProgressListener {
	// instance variables
	AppState state;
	private JFrame frame;
	HomePanel homePanel;
	User loggedInUser;
	ClientConsolePanel clientConsolePanel;
	DirectConnectPanel directConnectPanel;
	public ServerConsolePanel serverConsolePanel;
    ServerSelectPanel serverSelectPanel;
	ServerCreationPanel serverCreationPanel;
	EditProfilePanel editProfilePanel;
	/** Empty constructor of objects of class SomeClassUI. */
	public ApplicationManager() {
		loggedInUser=new User("New001");
		homePanel = new HomePanel(this,loggedInUser);
		clientConsolePanel=new ClientConsolePanel(this);
		directConnectPanel=new DirectConnectPanel(this);
		serverConsolePanel=new ServerConsolePanel(this);
        serverSelectPanel=new ServerSelectPanel(this);
        serverCreationPanel=new ServerCreationPanel(this);
        editProfilePanel=new EditProfilePanel(this,loggedInUser);
	}

	/** Interface initialization. */
	@Override
	public void run() {
		// These should handle their own component initialization.
		// They should, at least, receive a reference to the listener.
		frame = new JFrame("Launcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(homePanel);
		state=AppState.HOME;
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/** */
	@Override
	public void progressTo(AppState newState) {
		switch (newState) {
		case HOME:
			setPanel(homePanel);
			homePanel.onEnter();
			break;
		case CLIENT_CONSOLE:
			setPanel(clientConsolePanel);
			break;
		case DIRECT_CONNECT:
			setPanel(directConnectPanel);
			break;
		case EDIT_PROFILE:
			setPanel(editProfilePanel);
			editProfilePanel.onEnter();
			break;
		case SERVER_CONSOLE:
			setPanel(serverConsolePanel);
			break;
		case SERVER_SELECT:
            setPanel(serverSelectPanel);
            serverSelectPanel.updateServerList();
			break;
		case CREATE_SERVER:
			setPanel(serverCreationPanel);
			break;
		default:
			break;

		}
		state=newState;
		return;
	}

	private void setPanel(JPanel panel) {
		frame.setContentPane(panel);
		frame.pack();
	}

	/** */
	public void go() {
		SwingUtilities.invokeLater(this);
	}

	/** */
	public static void main(String[] args) {
		new ApplicationManager().go();
	}
	public void showMessageDialog(String message,String title,int type){
		JOptionPane.showMessageDialog(frame, message, title, type);

	}
	public User getUser(){
		return loggedInUser;
	}
}