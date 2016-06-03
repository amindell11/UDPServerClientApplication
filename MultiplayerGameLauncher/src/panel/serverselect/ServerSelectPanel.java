/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package panel.serverselect;

import java.awt.FlowLayout;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import multiplayergamelauncher.ProgressListener;
import net.connectionutil.ServerDiscoveryUtil;

/**
 *
 * @author amind_000
 */
public class ServerSelectPanel extends javax.swing.JPanel {
	protected Map<String, InetAddress> servers;
	FlowLayout layout;
	private JPanel selectSubPanel;
	private JPanel loadingSubPanel;
	protected ProgressListener listener;

	/**
	 * Creates new form ServerSelectPanel
	 */
	public ServerSelectPanel(ProgressListener listener) {
		loadingSubPanel = new LoadingServersSubPanel(this);
		selectSubPanel = new ServerSelectSubPanel(this);
		setPreferredSize(selectSubPanel.getPreferredSize());
		setMinimumSize(selectSubPanel.getPreferredSize());
		this.listener = listener;
		initComponents();
	}

	public void updateServerList() {
		removeAll();
		add(loadingSubPanel);
		revalidate();
		repaint();
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				try {
					servers = ServerDiscoveryUtil.getAvailableServers(1000);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			public void done() {
				removeAll();
				((ServerSelectSubPanel) selectSubPanel).enter();
				add(selectSubPanel);
				revalidate();
				repaint();
			}
		};
		worker.execute();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		layout = new FlowLayout();
		this.setLayout(layout);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables
}
