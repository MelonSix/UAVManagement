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
    private static DefaultMutableTreeNode scoutsNode;
    private static DefaultMutableTreeNode attackersNode;
    
    /**
     * Constructor for this class with the JTree component it will work with
     * @param tree The JTree object
     */
    public HandleTree(JTree tree) 
    {
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
        ArrayList<ReportedLwM2MClient> scoutDevices = getConnectedDevices(this.connectedDevices, "scout");
        ArrayList<ReportedLwM2MClient> attackerDevices = getConnectedDevices(this.connectedDevices, "attacker");
        System.out.println("Scouts num: "+scoutDevices.size()+", attacker num: "+attackerDevices.size());
        
        //gets scouts and their respective devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> scouts = getUAVAndOnboardDevices(scoutDevices);
        
        //gets attackers with their respective devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> attackers = getUAVAndOnboardDevices(attackerDevices);
        
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
    
    /**
     * Identifies an instance of a UAV and the devices/objects mounted on the UAV.
     * This method base on the device identifier nomenclature e.g. scout1-xxxx-xxxx-xxxx... to identify a UAV instance.
     * It splits the first part of the ID, which denotes the UAV instance that a device is mounted onto, and uses that to group all 
     * devices with the same UAV instance as devices mounted on the UAV instance.
     * @param connectedUavDevices Connected devices reported to the control center.
     * @return Mapping of a group of devices to a particular UAV instance
     */
    private TreeMap<String, ArrayList<ReportedLwM2MClient>> getUAVAndOnboardDevices(ArrayList<ReportedLwM2MClient> connectedUavDevices)
    {
        TreeMap<String, ArrayList<ReportedLwM2MClient>> uavWithOnboardDevices;
        uavWithOnboardDevices = new TreeMap<>();
        for(ReportedLwM2MClient reportedDevice : connectedUavDevices)
        {
            String uavLabel = reportedDevice.getEndpoint().split("-")[0];
            if(uavWithOnboardDevices.containsKey(uavLabel))
            {
                ArrayList<ReportedLwM2MClient> devices = uavWithOnboardDevices.get(uavLabel);
                devices.add(reportedDevice);
            }
            else
            {
                ArrayList<ReportedLwM2MClient> devices = new ArrayList<>();
                devices.add(reportedDevice);
                uavWithOnboardDevices.put(uavLabel, devices);
            }
        }
        return uavWithOnboardDevices;
    }
    
    /**
     * This method is used for categorization purposes. It groups the reported devices into UAV kinds
     * @param connectedDevices The reported devices/LwM2M clients
     * @param uavType The UAV kind to be used for the categorization (e.g. scout/attacker)
     * @return The list of connected devices which are of the specified <code>uavType</code>
     */
    private ArrayList<ReportedLwM2MClient> getConnectedDevices(ArrayList<ReportedLwM2MClient> connectedDevices, String uavType)
    {
        ArrayList<ReportedLwM2MClient> conDevices;
        conDevices = new ArrayList<>();
        for(ReportedLwM2MClient scoutDevice : connectedDevices)
        {
            String endpointname = scoutDevice.getEndpoint();
            if(endpointname.toLowerCase().contains(uavType))
            {
                conDevices.add(scoutDevice);
            }
        }
        return conDevices;
    }
    
}
