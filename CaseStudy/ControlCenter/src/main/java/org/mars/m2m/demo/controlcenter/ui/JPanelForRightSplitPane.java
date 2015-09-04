/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.ui;

import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author AG BRIGHTER
 */
final class JPanelForRightSplitPane extends JPanel
{

    public JPanelForRightSplitPane() {
        setPanelProperties();
        
        proofOfConcept();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    public void setPanelProperties()
    {
        setLayout(new GridLayout(10, 1, 2, 2));
    }
//</editor-fold>
    
    public void proofOfConcept()
    {
        ResourcePanel [] resourcePanels = new ResourcePanel[5];
        for(int i=0; i<resourcePanels.length; i++)
        {
            resourcePanels[i] = new ResourcePanel();
            add(resourcePanels[i]);
        }
    }
}
