/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.ui;

import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.mars.m2m.demo.controlcenter.analysis.ChartDatastore;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.ui.ControlCenter;

/**
 *
 * @author AG BRIGHTER
 */
public class AnalysisUI_LineChart extends JFrame
{
    private int inforSharingAlg;
    private String algStr;
    
    public AnalysisUI_LineChart(int inforSharingAlg, String algStr) 
    {
        super("Total Messages Chart - "+algStr);
        this.inforSharingAlg = inforSharingAlg;
        this.algStr = algStr;
        // This method is invoked on the EDT thread
        final JFXPanel fxPanel = new JFXPanel();
        JPanel jpanel = new JPanel();
        jpanel.add(fxPanel);
        this.add(jpanel);
        this.setSize(820, 650);
        this.setVisible(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() 
        {
            @Override
            public void run() 
            {
                SwingUtilities.invokeLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        Platform.runLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                initFX(fxPanel);
                            }
                        });
                    }
                });
            }
        }, 500, 5000);
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private Scene createScene() 
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setUpperBound(1000);
        final LineChart<Number,Number> lineChart =  new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Totoal Communication - "+this.algStr);
        xAxis.setLabel("Time (t)");       
        yAxis.setLabel("Messages (m)");
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Messages sent to UAVs at time t of simulation"); 
        
        ChartDatastore chartDatastore = new ChartDatastore();
        Map<Integer, Integer> gData = (inforSharingAlg==CC_StaticInitConfig.BROADCAST_INFOSHARE)?
                                        chartDatastore.getTotalMessagesSentInTimestep_broadcast():
                                        chartDatastore.getTotalMessagesSentInTimestep_register();
        Map<Integer, Integer> gDataSync = Collections.synchronizedMap(gData);
        for(Integer t : gDataSync.keySet())
        {
            series1.getData().add(new XYChart.Data(t, gData.get(t)));
        } 
        
        lineChart.getData().addAll(series1);
        Scene scene  = new Scene(lineChart,800,600);
        return scene;
    }
}
