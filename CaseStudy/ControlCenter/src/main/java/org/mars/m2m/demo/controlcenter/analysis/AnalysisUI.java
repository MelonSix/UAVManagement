/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import ch.qos.logback.classic.Logger;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AnalysisUI
{
    private static Logger logger = (Logger) LoggerFactory.getLogger(AnalysisUI.class);

    final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";
    
    public AnalysisUI() {
    }
    
    public static JFXPanel performAnalysisForGraph()
    {
        final JFXPanel fXPanel = new JFXPanel();
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
               Scene scene = creatScene();
               fXPanel.setScene(scene);
            }
        });
        return fXPanel;
    }
    
    private static Scene creatScene()
    {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Messages sent");
        xAxis.setLabel("Messages");       
        yAxis.setLabel("Time");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Messages sent at Time t");  
        Map<Integer, Integer> gData = GraphDatastore.getMessagesPerSecondData();
        for(Integer t : gData.keySet())
        {
            System.out.println("asdfas");
            series1.getData().add(new XYChart.Data(t, gData.get(t)));
        }
        
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        return scene;
    }
    
    
}
