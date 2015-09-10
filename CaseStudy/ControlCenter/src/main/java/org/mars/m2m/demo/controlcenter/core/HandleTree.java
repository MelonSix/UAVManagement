/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.core;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.util.UavUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class HandleTree implements TreeSelectionListener
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HandleTree.class);
    private ArrayList<ReportedLwM2MClient> connectedDevices;
    private JTree tree;
    private DefaultTreeModel treeModel;
    public static DefaultMutableTreeNode scoutsNode;
    public static DefaultMutableTreeNode attackersNode;
    private UavUtil uavUtil;
    
    /**
     * Constructor for this class with the JTree component it will work with
     * @param tree The JTree object
     */
    public HandleTree(JTree tree) 
    {
        this.uavUtil = new UavUtil();
        if(tree != null)
        {
            this.tree = tree;
            initializeTree();
        }
    }
    
    /**
     * Performs initialization procedures on the JTree
     */
    private void initializeTree()
    {
        treeModel = (DefaultTreeModel) this.tree.getModel();
        this.tree.addTreeSelectionListener(this);
        scoutsNode = new DefaultMutableTreeNode("Scouts");
        attackersNode = new DefaultMutableTreeNode("Attackers");
        DefaultMutableTreeNode root = null;
        root = new DefaultMutableTreeNode("Control Center");
        root.add(scoutsNode);
        root.add(attackersNode);
        
        treeModel.setRoot(root);
    }
    
    /**
     * Handles node selection events
     * @param e The event object
     */
    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        /**
         * Converts a selected node of the tree back to an instance of ReportedLwM2MClient
         */
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
        if(node == null)
        return;        
        Object nodeInfo;
        nodeInfo = node.getUserObject();
        ReportedLwM2MClient device = (ReportedLwM2MClient) nodeInfo;
        System.out.println(device.getAddress()+", "+device.getRegistrationId());
    }
    
    /**
     * Populates the JTree of this {@link HandleTree} instance with the necessary nodes
     * @param devices The newly connected devices(LwM2M clients) data submitted to the Control center (Scouts & Attackers)
     */
    public void populateJTree(ArrayList<ReportedLwM2MClient> devices)
    {
        //updates the connected devices list of the control center.
        this.connectedDevices = devices;
        
        //categorizes the reported devices
        ArrayList<ReportedLwM2MClient> scoutDevices = uavUtil.getConnectedDevicesByCategory(this.connectedDevices, "scout");
        ArrayList<ReportedLwM2MClient> attackerDevices = uavUtil.getConnectedDevicesByCategory(this.connectedDevices, "attacker");
        System.out.println("Scouts num: "+scoutDevices.size()+", attacker num: "+attackerDevices.size());
        
        //gets scouts and their respective devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> scouts = uavUtil.getUAVAndOnboardDevices(scoutDevices);
        
        //gets attackers with their respective devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> attackers = uavUtil.getUAVAndOnboardDevices(attackerDevices);
        
        /**
         * Resets the UAV nodes because any call to this method submits a fresh list of all
         * Connected devices meaning previously listed nodes has to be removed to avoid duplicates
         */
        scoutsNode.removeAllChildren();
        attackersNode.removeAllChildren();
        
        //lists the newly connected scouts
        populateNode(scouts, scoutsNode);
        
        //lists the newly connected attackers
        populateNode(attackers, attackersNode);
        
    }
    
    /**
     * Populates a list of available UAVs of a particular UAV kind (Scouts/Attackers) under the node of that particular UAV kind
     * @param UAVs The UAVs of a particular UAV kind (Scouts/Attackers)
     * @param uavKindNode The tree node dedicated to a particular UAV kind
     */
    private void populateNode(final TreeMap<String, ArrayList<ReportedLwM2MClient>> UAVs, final DefaultMutableTreeNode uavKindNode) 
    {
        //registers a UI component update with the Event Dispatch Thread (EDT)
        SwingWorker<JTree,Void> swingWorker; 
        swingWorker = new SwingWorker<JTree, Void>() 
        {
            @Override
            protected JTree doInBackground() throws Exception
            {
                for(String uavLabel : UAVs.keySet())
                {
                    DefaultMutableTreeNode uavNode = new DefaultMutableTreeNode(uavLabel);
                    ArrayList<ReportedLwM2MClient> onBoardDevices = UAVs.get(uavLabel);
                    for(ReportedLwM2MClient device : onBoardDevices)
                    {
                        DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(device);
                        uavNode.add(deviceNode);
                    }
                    //decides where to add a particular UAV
                    uavKindNode.add(uavNode);
                }
                return tree;
            }
            
            @Override
            protected void done() 
            {
                JTree jTree;
                try 
                {
                    jTree = get();
                    //updates the JTree component to show the latest nodes
                    jTree.updateUI();
                } catch (InterruptedException | ExecutionException ex) {
                    logger.error(ex.toString());
                }
            }            
        };
        swingWorker.execute();
    }
    
    
}
