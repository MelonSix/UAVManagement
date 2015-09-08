/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.core;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.JTree;
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

    public HandleTree(JTree tree) 
    {
        if(tree != null)
        {
            this.tree = tree;
            initializeTree();
        }
    }
    
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
    
    public void populateJTree(ArrayList<ReportedLwM2MClient> devices)
    {
        this.connectedDevices = devices;
        
        //categorizes the reported devices
        ArrayList<ReportedLwM2MClient> scoutDevices = getConnectedDevices(this.connectedDevices, "scout");
        ArrayList<ReportedLwM2MClient> attackerDevices = getConnectedDevices(this.connectedDevices, "attacker");
        System.out.println("Scouts num: "+scoutDevices.size()+", attacker num: "+attackerDevices.size());
        
        //gets scouts and their respective devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> scouts = getScouts(scoutDevices);
        
        //gets attackers with their respective devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> attackers = getScouts(attackerDevices);
        
        //resets the uav nodes
        scoutsNode.removeAllChildren();
        attackersNode.removeAllChildren();
        
        //lists the newly connected scouts
        populateNode(scouts, scoutsNode);
        
        //lists the newly connected attackers
        populateNode(attackers, attackersNode);
        
    }

    private void populateNode(TreeMap<String, ArrayList<ReportedLwM2MClient>> uavKind, DefaultMutableTreeNode uavKindNode) 
    {
        for(String uavLabel : uavKind.keySet())
        {
            DefaultMutableTreeNode uavNode = new DefaultMutableTreeNode(uavLabel);
            ArrayList<ReportedLwM2MClient> onBoardDevices = uavKind.get(uavLabel);
            for(ReportedLwM2MClient device : onBoardDevices)
            {
                DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(device);
                uavNode.add(deviceNode);
            }
            //decides where to add a particular UAV
            uavKindNode.add(uavNode);
        }
        //updates the JTree component to show the latest nodes
        tree.updateUI();
    }
    
    private TreeMap<String, ArrayList<ReportedLwM2MClient>> getScouts(ArrayList<ReportedLwM2MClient> connectedUavDevices)
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
