/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mars.m2m.demo.config.OpStaticInitConfig;
import org.mars.m2m.demo.ui.AnimationPanel;
import org.mars.m2m.demo.ui.ControlPanel;

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
            long start=System.nanoTime();
            this.animPnl.clearUAVImageBeforeUpdate();
            if (OpStaticInitConfig.SIMULATION_ON) 
            {
                int simStep = this.animPnl.getSimulation_time_step();
                this.animPnl.setSimulation_time_step(++simStep);
                    //System.out.println("timestep "+this.animPnl.getSimulation_time_step());
                this.animPnl.getWorld().updateAll();
                int minimutes = this.animPnl.getSimulation_time_step();
                int hours = minimutes / 60;
                minimutes -= hours*60;
                String simulated_time_str = String.format("%1$02d:%2$02d:%3$02d", hours, minimutes, 0);
                ControlPanel.jFormattedTextField1.setText(simulated_time_str);
                ControlPanel.setTotalHistoryPathLen(animPnl.getWorld().getTotal_path_len());
            }
            this.animPnl.updateImageAtEachIteration();
            animPnl.repaint();           
            System.out.println("whole time:"+(System.nanoTime()-start)*1e-9);
        }
    }
