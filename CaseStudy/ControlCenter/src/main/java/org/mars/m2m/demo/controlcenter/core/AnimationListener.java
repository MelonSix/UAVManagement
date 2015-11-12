/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.services.ReadAttackers;
import org.mars.m2m.demo.controlcenter.ui.AnimationPanel;

/**
 *
 * @author Yulin_Zhang
 */
/** 
     * 
     */
public class AnimationListener implements ActionListener 
{
        AnimationPanel animPnl;

        public AnimationListener(AnimationPanel animationPanel)
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
            updateGUI();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    updateAll(animPnl);
                }
            }).start();
//            ExecutorService executor = Executors.newFixedThreadPool(2);
//            executor.execute(new Runnable() {
//
//                @Override
//                public void run() {                    
//                    updateGUI();
//                }
//            });
//            executor.execute(new Runnable() {
//
//                @Override
//                public void run() {
//                    updateAll(animPnl);
//                }
//            });
//            executor.shutdown();
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
        final ControlCenterServices cc = panel.getControlCenterServices();
        if (cc.isSimulationStartable()) 
        {
            /**
            * if any obstacle or threat has ever been reported then simulation can start
            * and CC can perform operations on endpoint clients
            */
            ReadAttackers.readAttackerResources();
            cc.registerInfoRequirement();
            cc.shareInfoAfterRegistration();
            cc.roleAssignmentInControlCenter();
        }
    }
}
