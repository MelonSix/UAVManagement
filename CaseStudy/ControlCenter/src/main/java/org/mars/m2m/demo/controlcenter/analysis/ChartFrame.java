/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author AG BRIGHTER
 */
public class ChartFrame
{
    JFXPanel fxPanel;
    public ChartFrame()
    {
      initSwingComponents();
    }

    public JPanel initSwingComponents()
    {
      JPanel mainPanel = new JPanel(new BorderLayout());
      fxPanel = new JFXPanel();
      mainPanel.add(fxPanel, BorderLayout.CENTER);      
      initFxComponents();
      return mainPanel;
    }

    private void initFxComponents(){

      Platform.runLater(new Runnable() {
        @Override
        public void run() {
            GridPane grid = new GridPane();
            Scene scene = new Scene(grid, 800, 400);

            /**
             * Construct and populate Bar chart.
             * It uses 2 series of data.
             */
            NumberAxis lineYAxis =
              new NumberAxis(0,100_000,10_000);
            lineYAxis.setLabel("Sales");
            CategoryAxis lineXAxis = new CategoryAxis();
            lineXAxis.setLabel("Products");
            BarChart barChart =
              new BarChart<>(lineXAxis,lineYAxis);
            XYChart.Series bar1 =
              new XYChart.Series<>();
            bar1.setName("Computing Devices");
            bar1.getData().add(getData(40000,"Desktop"));
            bar1.getData().add(getData(30_000,"Netbooks"));
            bar1.getData().add(getData(70_000,"Tablets"));
            bar1.getData().add(getData(90_000,"Smartphones"));

            XYChart.Series bar2 = new XYChart.Series<>();
            bar2.setName("Consumer Goods");
            bar2.getData().add(getData(60_000,"Washing Machines"));
            bar2.getData().add(getData(70_000,"Telivision"));
            bar2.getData().add(getData(50_000,"Microwave Ovens"));

            barChart.getData().addAll(bar1,bar2);
            grid.setVgap(20);
            grid.setHgap(20);
            grid.add(barChart,2,0);
            fxPanel.setScene(scene);
          }
        });

    }

    private XYChart.Data getData(double x, double y){
      XYChart.Data data = new XYChart.Data<>();
      data.setXValue(x);
      data.setYValue(y);
      return data;
    }

    private XYChart.Data getData(double x, String y){
      XYChart.Data data = new XYChart.Data<>();
      data.setYValue(x);
      data.setXValue(y);
      return data;
    }
}

