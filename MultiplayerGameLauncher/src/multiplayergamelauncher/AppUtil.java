package multiplayergamelauncher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import net.Config;
import net.client.ClientThread;
import net.client.MembershipRequestDeniedException;

public class AppUtil {
	public static ClientThread createClient(final String address,int port,String username,final ApplicationManager listener){
		final ClientThread client = new ClientThread(username, address);
		final SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws Exception {
				String errorMessage = "Unkown server error. Please try again later.";
				try {
					client.requestClusterMembership(address, Config.PORT);
				} catch (MembershipRequestDeniedException e) {
					errorMessage=e.getMessage();
				}
				return errorMessage;
			}

		};
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (client.isActiveMember()) {
					listener.progressTo(AppState.CLIENT_CONSOLE);
				} else {
					try {
						listener.showMessageDialog(worker.get(), "Error: Failed to connect",
								JOptionPane.ERROR_MESSAGE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		});
		worker.execute();
		return client;
	}
}
