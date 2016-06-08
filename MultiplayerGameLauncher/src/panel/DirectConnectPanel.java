/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package panel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import hooks.GameHooks;
import multiplayergamelauncher.AppState;
import multiplayergamelauncher.ApplicationManager;
import net.Config;
import net.client.ClientThread;
import net.connectionutil.ServerDiscoveryUtil;
import net.server.ServerInfo;

/**
 *
 * @author amind_000
 */
public class DirectConnectPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6989293314807365432L;
	String address;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton backButton;

	private javax.swing.JButton checkAddressButton;

	private javax.swing.JButton connectButton;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabel2;

	private javax.swing.JLabel jLabel3;

	private javax.swing.JLabel jLabel4;

	private javax.swing.JLabel jLabel5;

	private javax.swing.JLabel jLabel7;

	private javax.swing.JLabel jLabel9;
	private javax.swing.JTextField jTextField1;
	private ApplicationManager listener;
	private javax.swing.JLabel playerNumLabel;
	private javax.swing.JLabel serverAddressLabel;
	private javax.swing.JLabel serverNameLabel;
	// End of variables declaration//GEN-END:variables
	/**
	 * Creates new form DirectConnect
	 */
	public DirectConnectPanel(ApplicationManager listener) {
		initComponents();
		this.listener = listener;
	}
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_backButtonActionPerformed
		listener.progressTo(AppState.HOME);
	}// GEN-LAST:event_backButtonActionPerformed
	public void checkAddress(String ip) {
		serverAddressLabel.setText(ip);
		jLabel3.setText("Checking...");
		final SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
			@Override
			public Boolean doInBackground() {
				return (address != null && ServerDiscoveryUtil.checkAddressForServer(address, Config.PORT, 1000));
			}

			@Override
			public void done() {

			}
		};
		worker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
					try {
						if (worker.get()) {
							updateInfoArea(ServerDiscoveryUtil.getServerInfo(address, Config.PORT));
							jLabel3.setText("Server available");
						} else {
							updateInfoArea(new ServerInfo("", 0, "", 0, 0));
							jLabel3.setText("No server at requested address");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
		worker.execute();

	}
	private void checkAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_checkAddressButtonActionPerformed
		address = jTextField1.getText();
		checkAddress(address);
	}// GEN-LAST:event_checkAddressButtonActionPerformed:
	private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectButtonActionPerformed
		GameHooks.createClient(address,Config.PORT,listener.getUser().getName(),listener);
	}// GEN-LAST:event_connectButtonActionPerformed
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		checkAddressButton = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		connectButton = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		playerNumLabel = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		serverNameLabel = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		serverAddressLabel = new javax.swing.JLabel();
		backButton = new javax.swing.JButton();

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
		jLabel1.setText("Direct Connect");

		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		checkAddressButton.setText("Check Address");
		checkAddressButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				checkAddressButtonActionPerformed(evt);
			}
		});

		jLabel3.setText("Checking...");

		jLabel4.setText("Server Info:");

		jLabel5.setText("Enter Server Address");

		connectButton.setText("Connect To Server");
		connectButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectButtonActionPerformed(evt);
			}
		});

		jLabel2.setText("Players: ");

		playerNumLabel.setText("16/16");

		jLabel7.setText("Name:");

		serverNameLabel.setText("TestName");

		jLabel9.setText("Address:");

		serverAddressLabel.setText("10.208.1.1");

		backButton.setText("Back");
		backButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup().addContainerGap(110, Short.MAX_VALUE)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(backButton)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(jLabel5).addComponent(jLabel4)
														.addComponent(jLabel3)
														.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
																178, Short.MAX_VALUE)
										.addComponent(jTextField1)
										.addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(checkAddressButton, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(layout.createSequentialGroup().addGap(10, 10, 10)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(layout.createSequentialGroup().addComponent(jLabel9)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(serverAddressLabel))
												.addGroup(layout.createSequentialGroup().addComponent(jLabel7)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(serverNameLabel))
												.addGroup(layout.createSequentialGroup().addComponent(jLabel2)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(playerNumLabel))))))
										.addGap(110, 110, 110)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18)
						.addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(checkAddressButton)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7)
						.addComponent(serverNameLabel))
				.addGap(3, 3, 3)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2)
						.addComponent(playerNumLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9)
						.addComponent(serverAddressLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(connectButton)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(backButton)
				.addGap(36, 36, 36)));
	}// </editor-fold>//GEN-END:initComponents
	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField1ActionPerformed
	private void updateInfoArea(ServerInfo info) {
		serverAddressLabel.setText(info.getAddress());
		serverNameLabel.setText(info.getServerName());
		playerNumLabel.setText(info.getNumClients() + "/" + info.getMaxClients());
	}
}
