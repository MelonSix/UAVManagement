/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.application;

import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.client.Client;
import org.mars.m2m.managementserver.ListenersImpl.DeviceReporterImpl;

/**
 *
 * @author AG BRIGHTER
 */
public class ConfigUI extends javax.swing.JFrame {
private final LwM2mServer server;
    /**
     * Creates new form ConfigUI
     */
    public ConfigUI(LwM2mServer server) {
        this.server = server;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        JMenuItem_ClrRegistry = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Management Server Config UI");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Config");

        JMenuItem_ClrRegistry.setText("Clear Registry");
        JMenuItem_ClrRegistry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMenuItem_ClrRegistryActionPerformed(evt);
            }
        });
        jMenu2.add(JMenuItem_ClrRegistry);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JMenuItem_ClrRegistryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMenuItem_ClrRegistryActionPerformed
        synchronized(server)
        {
            for(Client client : server.getClientRegistry().allClients())
            {
                server.getClientRegistry().deregisterClient(client.getRegistrationId());
                if(server.getClientRegistry().allClients().isEmpty())
                {
                    System.out.println("All clients successfully deregistered");
                }
                else
                    System.out.println("WARNING!! Clients registry not empty");
            }
            DeviceReporterImpl.counter = 0;
        }
    }//GEN-LAST:event_JMenuItem_ClrRegistryActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem JMenuItem_ClrRegistry;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
}
