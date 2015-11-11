/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.ui;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

/**
 *
 * @author AG BRIGHTER
 */
public class FieldUI extends javax.swing.JFrame {

    /**
     * Creates new form FieldUI
     */
    public FieldUI() {
        this.animationPanel = new AnimationPanel();
        initComponents();
        //starts animation depending on scout(s) status
        boolean wait_for_scouts = true;
        boolean are_scouts_ready = false;
        if(animationPanel.getWorld() == null) System.out.println("null");
        this.animationPanel.start();
//        synchronized(animationPanel)
//        {
//            do
//            {
//                if(this.animationPanel.getScouts() != null) 
//                {
//                    for (Scout scout : this.animationPanel.getScouts()) {
//                        if (scout.getMove_at_y_coordinate_task().size() > 0) {
//                            are_scouts_ready = true;
//                        } else {
//                            are_scouts_ready = false;
//                        }
//                    }
//                    if (are_scouts_ready) {
//                        //begins animation after initialization procedures and scout waypoints are available
//                        wait_for_scouts = false;
//                        
//                    }
//                }
//            }while(wait_for_scouts);
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        opFieldSplitPane = new javax.swing.JSplitPane();
        sidebar1 = new org.mars.m2m.demo.ui.Sidebar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 603));
        setName("UAV Operational Field"); // NOI18N

        opFieldSplitPane.setDividerLocation(250);
        opFieldSplitPane.setResizeWeight(0.5);
        opFieldSplitPane.setMaximumSize(null);
        this.opFieldSplitPane.add(animationPanel, JSplitPane.RIGHT);
        opFieldSplitPane.setLeftComponent(sidebar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(opFieldSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(opFieldSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(FieldUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(FieldUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(FieldUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(FieldUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    FieldUI opUI = new FieldUI();
                    opUI.setVisible(true);
                    opUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    opUI.setResizable(false);
                    opUI.opFieldSplitPane.setDividerLocation(100);
                    opUI.opFieldSplitPane.updateUI();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JSplitPane opFieldSplitPane;
    private AnimationPanel animationPanel;
    private org.mars.m2m.demo.ui.Sidebar sidebar1;
    // End of variables declaration//GEN-END:variables
}
