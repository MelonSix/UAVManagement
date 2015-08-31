/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.operationfield.ui;

import ch.qos.logback.classic.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.mars.m2m.demo.operationfield.config.StaticInitConfig;
import org.mars.m2m.demo.operationfield.world.ControlCenter;
import org.mars.m2m.demo.operationfield.world.model.Threat;
import org.slf4j.LoggerFactory;

/**
 *
 * @author boluo
 */
public class MyPopupMenu extends JPopupMenu implements ActionListener {

    ArrayList<JMenuItem> items;
    ControlCenter control_center;
    private static Logger logger = (Logger) LoggerFactory.getLogger(MyPopupMenu.class);

    /** internal variable
     * 
     */
    private int choosen_attacker_index=-1;
    
    public MyPopupMenu(ControlCenter control_center) {
        super();
        this.control_center = control_center;
        items = new ArrayList<JMenuItem>();
        ArrayList<Threat> threats = this.control_center.getThreats();
        for (Threat threat : threats) {
            JMenuItem threat_item = new JMenuItem(StaticInitConfig.THREAT_NAME+threat.getIndex());
            items.add(threat_item);
            this.add(threat_item);
            threat_item.addActionListener(this);
        }

    }
    
    public void setChoosedAttackerIndex(int attacker_index)
    {
        this.choosen_attacker_index=attacker_index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = items.indexOf(e.getSource());
        control_center.roleAssignForAttackerWithSubTeam(choosen_attacker_index, index);
        StaticInitConfig.SIMULATION_ON = true;
    }

}
