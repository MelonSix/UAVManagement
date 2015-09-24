/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingWorker;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.ui.AnimationPanel;

/**
 *
 * @author Yulin_Zhang
 */
/** 
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
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(new Runnable() {

                @Override
                public void run() {                    
                    updateGUI();
                }
            });
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    updateAll(animPnl);
                }
            });
            executor.shutdown();
        }

    private void updateGUI() {
        SwingWorker<AnimationPanel,Void> swingWorker = new SwingWorker<AnimationPanel, Void>() {
            
            @Override
            protected AnimationPanel doInBackground() throws Exception {
                return animPnl;
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
        
    private void updateAll(AnimationPanel panel) 
    {
        ControlCenterServices cc = panel.getControlCenterServices();

        synchronized(cc)
        {                
            if (cc.isSimulationStartable()) //if any obstacle or threat has ever been reported
            {
                cc.registerInfoRequirement();
                cc.shareInfoAfterRegistration();
                cc.roleAssignmentInControlCenter();
            }
        }
    }
}
