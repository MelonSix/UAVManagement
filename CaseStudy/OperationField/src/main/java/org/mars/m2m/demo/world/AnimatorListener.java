/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import org.mars.m2m.demo.config.StaticInitConfig;
import org.mars.m2m.demo.ui.AnimationPanel;

/**
 *
 * @author Yulin_Zhang
 */
/** This class implement the thread to drive the world, and paint the objects on the panel.
     * 
     */
public class AnimatorListener implements ActionListener 
{
        AnimationPanel animPnl;

        public AnimatorListener(AnimationPanel animationPanel)
        {
            this.animPnl = animationPanel;
        }

        /** The implemented function of the thread.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int simStep = this.animPnl.getSimulation_time_step();
            System.out.println("timestep "+this.animPnl.getSimulation_time_step());
            
            this.animPnl.clearUAVImageBeforeUpdate();
            if (StaticInitConfig.SIMULATION_ON) {
                this.animPnl.setSimulation_time_step(++simStep);
                this.animPnl.getWorld().updateAll();
                int minimutes = this.animPnl.getSimulation_time_step();
                int hours = minimutes / 60;
                minimutes=minimutes-hours*60;
                String simulated_time_str = String.format("%1$02d:%2$02d:%3$02d", hours, minimutes, 0);
                //ControlPanel.jFormattedTextField1.setText(simulated_time_str);
                //ControlPanel.setTotalHistoryPathLen(world.getTotal_path_len());
            }
            this.animPnl.updateImageAtEachIteration();
            animPnl.repaint();
            if (this.animPnl.getWorld().isExperiment_over()) {
                System.exit(0);
            }
        }
    }
