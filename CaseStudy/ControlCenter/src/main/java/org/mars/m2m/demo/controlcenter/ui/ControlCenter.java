/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.ui;

import ch.qos.logback.classic.Logger;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
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
    /**
     * Creates new form ControlCenter
     * @param ccs
     */
    public ControlCenter(ControlCenterServices ccs) {
        this.controlCenterServices = ccs;//has to come before initialization of components because the object is used in UI customized code
        initComponents();
        handleTree = new HandleTree(jTreeControlCenter);
        NewDeviceServices.setHandleTree(handleTree);
        ccSplitPanelTab1.add(this.pnlRight, JSplitPane.RIGHT);
        this.animationPanel.start();
    }

    public HandleTree getHandleTree() {
        return handleTree;
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
        ccMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Control Center v1.0.0");
        setMaximumSize(new java.awt.Dimension(900, 700));
        setMinimumSize(new java.awt.Dimension(900, 700));

        ccSplitPanelTab1.setDividerLocation(150);
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

        jSplitPane1.setDividerLocation(600);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        this.animationPanel = new AnimationPanel(this.controlCenterServices);

        this.jSplitPane1.add(animationPanel, JSplitPane.TOP);

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

        jMenu1.setText("File");
        ccMenuBar.add(jMenu1);

        jMenu2.setText("Edit");
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JSplitPane jSplitPane1;
    private AnimationPanel animationPanel;
    private javax.swing.JTree jTreeControlCenter;
    // End of variables declaration//GEN-END:variables

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
