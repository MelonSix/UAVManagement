/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.services;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.util.AttackerUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ReadAttackers 
{   
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ReadAttackers.class);
    private static ArrayList<AttackerModel> attackers = new ArrayList<>();

    public ReadAttackers() {
    }
    
    public static void readAttackerResources()
    {   
        ArrayList<AttackerModel> attackers_partial = new ArrayList<>();
        int attacker_num = HandleTree.attackersNode.getChildCount();
        for (int i = 0; i < attacker_num; i++) 
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) HandleTree.attackersNode.getChildAt(i).getChildAt(0);
            if (node != null) 
            {
                try
                {
                    AttackerModel attacker = AttackerUtils.getVirtualizedAttacker(node);
                    if(attacker != null)
                        attackers_partial.add(attacker);
                }
                catch(NullPointerException e)
                {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
        synchronized(attackers)
        {
            attackers.clear();
            attackers.addAll(attackers_partial);
        }
    }

    public synchronized static ArrayList<AttackerModel> getAttackers() {
        return attackers;
    }
    
}
