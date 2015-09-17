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
            SwingWorker<AnimationPanel,Void> swingWorker = new SwingWorker<AnimationPanel, Void>() {

                @Override
                protected AnimationPanel doInBackground() throws Exception {
                    return animPnl;
                }

                @Override
                protected void done() {
                    try 
                    {                        
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
    }
