package multiplayergamelauncher;

import panel.serverselect.ServerSelectPanel;
import sun.font.CreatedFontTracker;

import java.awt.event.*;
import javax.swing.*;

import com.sun.jmx.remote.internal.ClientNotifForwarder;

import panel.*;
/**
 *
 * @author amind_000
 */
public class ApplicationManager implements Runnable, ProgressListener {
	// instance variables
	AppState state;
	private JFrame frame;
	HomePanel homePanel;
	ClientConsolePanel clientConsolePanel;
	DirectConnectPanel directConnectPanel;
	ServerConsolePanel serverConsolePanel;
    ServerSelectPanel serverSelectPanel;
	ServerCreationPanel serverCreationPanel;
	EditProfilePanel editProfilePanel;
	/** Empty constructor of objects of class SomeClassUI. */
	public ApplicationManager() {
		homePanel = new HomePanel(this);
		clientConsolePanel=new ClientConsolePanel(this);
		directConnectPanel=new DirectConnectPanel(this);
		serverConsolePanel=new ServerConsolePanel(this);
        serverSelectPanel=new ServerSelectPanel(this);
        serverCreationPanel=new ServerCreationPanel(this);
        editProfilePanel=new EditProfilePanel(this);
	}

	/** Interface initialization. */
	@Override
	public void run() {
		// These should handle their own component initialization.
		// They should, at least, receive a reference to the listener.
		try { 
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
		} catch (Exception ex) { 
		    ex.printStackTrace(); 
		}
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
			break;
		case CLIENT_CONSOLE:
			setPanel(clientConsolePanel);
			break;
		case DIRECT_CONNECT:
			setPanel(directConnectPanel);
			break;
		case EDIT_PROFILE:
			setPanel(editProfilePanel);
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
}