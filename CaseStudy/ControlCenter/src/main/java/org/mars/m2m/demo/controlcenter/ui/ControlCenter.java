/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.ui;

import ch.qos.logback.classic.Logger;
import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.mars.m2m.demo.controlcenter.analysis.ChartDatastore;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
import org.mars.m2m.demo.controlcenter.core.LoadUAVs;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.services.NewDeviceServices;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public final class ControlCenter extends javax.swing.JFrame implements TreeSelectionListener
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ControlCenter.class);
    private final HandleTree handleTree;
    private final ControlCenterServices controlCenterServices;
    private final File file;
    /**
     * Creates new form ControlCenter
     * @param ccs
     */
    public ControlCenter(ControlCenterServices ccs) {
        file = new File("gdata.txt");
        this.controlCenterServices = ccs;//has to come before initialization of components because the object is used in UI customized code
        initComponents();
        handleTree = new HandleTree(jTreeControlCenter);
        NewDeviceServices.setHandleTree(handleTree);
        ccSplitPanelTab1.add(this.pnlRight, JSplitPane.RIGHT);
        ControlCenter.animationPanel.initComponents();
    }

    public HandleTree getHandleTree() {
        return handleTree;
    }
    
    private void updateGUI() 
    {
        SwingWorker<AnimationPanel,Void> swingWorker;
        swingWorker = new SwingWorker<AnimationPanel, Void>() {
            
            @Override
            protected AnimationPanel doInBackground() throws Exception {
                return animationPanel;
            }
            
            @Override
            protected void done() {
                try
                {
                    ControlCenterServices.time_step++;
                    AnimationPanel panel = get();
                    panel.clearUAVImageBeforeUpdate();
                    panel.updateImageAtEachIteration();
                    panel.repaint();
                } catch (InterruptedException | ExecutionException e) {
                    
                }
                
            }
        };
        swingWorker.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ccTabbedPane = new javax.swing.JTabbedPane();
        ccJPanelInTab1 = new javax.swing.JPanel();
        ccSplitPanelTab1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeControlCenter = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTree_ccKB = new javax.swing.JTree();
        ccMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        reloadUavMenuItem = new javax.swing.JMenuItem();
        viewChartMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        JMenuItem_ClrCCknowledge = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Control Center v1.0.0");
        setMinimumSize(new java.awt.Dimension(900, 700));

        ccSplitPanelTab1.setDividerLocation(300);
        this.pnlLeft = new JPanelForLeftSplitPane();
        this.pnlRight = new JPanelForRightSplitPane();

        jScrollPane1.setViewportView(jTreeControlCenter);

        ccSplitPanelTab1.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout ccJPanelInTab1Layout = new javax.swing.GroupLayout(ccJPanelInTab1);
        ccJPanelInTab1.setLayout(ccJPanelInTab1Layout);
        ccJPanelInTab1Layout.setHorizontalGroup(
            ccJPanelInTab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ccSplitPanelTab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE)
        );
        ccJPanelInTab1Layout.setVerticalGroup(
            ccJPanelInTab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ccSplitPanelTab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );

        ccTabbedPane.addTab("UAV", ccJPanelInTab1);

        jSplitPane1.setResizeWeight(0.9);
        this.animationPanel = new AnimationPanel(this.controlCenterServices);

        this.jSplitPane1.add(animationPanel, JSplitPane.TOP);

        jScrollPane2.setViewportView(JTree_ccKB);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );

        ccTabbedPane.addTab("Field", jPanel2);

        jMenu1.setText("Management");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        reloadUavMenuItem.setText("(Re) Load UAVs");
        reloadUavMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadUavMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(reloadUavMenuItem);

        viewChartMenuItem.setText("View Chart");
        viewChartMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewChartMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(viewChartMenuItem);

        ccMenuBar.add(jMenu1);

        jMenu2.setText("Edit");

        JMenuItem_ClrCCknowledge.setText("Clear Map");
        JMenuItem_ClrCCknowledge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMenuItem_ClrCCknowledgeActionPerformed(evt);
            }
        });
        jMenu2.add(JMenuItem_ClrCCknowledge);

        ccMenuBar.add(jMenu2);

        setJMenuBar(ccMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ccTabbedPane))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ccTabbedPane))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void reloadUavMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadUavMenuItemActionPerformed
        LoadUAVs loadUAVs = new LoadUAVs();
        loadUAVs.loadMgmntAdpterClients(animationPanel);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {                       
                        controlCenterServices.updateGUI();
                    }
                });
            }
        }, 500, 500);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {                
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JTree_ccKB.setModel(controlCenterServices.getKb()); 
                        JTree_ccKB.updateUI();
                    }
                });
            }
        }, 1000, 5000);
                
        CC_StaticInitConfig.CURRENT_SIMULATION_TIME.set(1);
        CC_StaticInitConfig.TOTAL_MESSAGES_SENT.set(0);
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {

            @Override
            public void run() {
                int current_simulation_time
                        = CC_StaticInitConfig.CURRENT_SIMULATION_TIME.incrementAndGet();
                System.out.println("Current simulation timestep: "+current_simulation_time);
                CC_StaticInitConfig.TOTAL_MESSAGES_SENT_IN_CURRENT_SIMULATION_TIMESTEP.set(0);
                Map<Integer, Integer> gData = ChartDatastore.getMessagesPerSecondData();
                synchronized(gData)
                {
                    for(Integer time : gData.keySet())
                    {
                        System.out.println("t"+time+" : "+gData.get(time)+" messages sent");
                    }
                }
            }
        }, 1000, 1000);
    }//GEN-LAST:event_reloadUavMenuItemActionPerformed

    private void JMenuItem_ClrCCknowledgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMenuItem_ClrCCknowledgeActionPerformed
        synchronized(animationPanel)
        {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    controlCenterServices.getConflicts().clear();
                    controlCenterServices.getThreats().clear();
                    controlCenterServices.getObstacles().clear();
                   animationPanel.clearMap();
                   animationPanel.repaint();
                   System.out.println("Map cleared");
                   
                    
                }
            });
        }
    }//GEN-LAST:event_JMenuItem_ClrCCknowledgeActionPerformed

    private void viewChartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewChartMenuItemActionPerformed
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                AnalysisChart analysisChart = new AnalysisChart();
                analysisChart.setVisible(true);
            }
        });
        
    }//GEN-LAST:event_viewChartMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem JMenuItem_ClrCCknowledge;
    private javax.swing.JTree JTree_ccKB;
    private javax.swing.JPanel ccJPanelInTab1;
    private javax.swing.JMenuBar ccMenuBar;
    private javax.swing.JSplitPane ccSplitPanelTab1;
    private JPanelForLeftSplitPane pnlLeft;
    private JPanelForRightSplitPane pnlRight;
    private javax.swing.JTabbedPane ccTabbedPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    public static AnimationPanel animationPanel;
    private javax.swing.JTree jTreeControlCenter;
    private javax.swing.JMenuItem reloadUavMenuItem;
    private javax.swing.JMenuItem viewChartMenuItem;
    // End of variables declaration//GEN-END:variables

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
