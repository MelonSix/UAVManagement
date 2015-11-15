/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import javafx.embed.swing.JFXPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Test {

    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Swing and JavaFX");
        
                    ChartFrame chartDemo = new ChartFrame();
        final JFXPanel fxPanel = new JFXPanel();
        BarChart barChart = new BarChart();
        frame.add(barChart.creatJFreeChart());
        //frame.getContentPane().add(chartDemo.initSwingComponents());
        frame.setSize(850, 650);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }
}
