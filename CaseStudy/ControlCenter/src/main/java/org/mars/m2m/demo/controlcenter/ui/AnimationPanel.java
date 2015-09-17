/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.ui;

import ch.qos.logback.classic.Logger;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.appConfig.GraphicConfig;
import org.mars.m2m.demo.controlcenter.core.AnimatorListener;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.util.ImageUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AnimationPanel extends JPanel
{
    private static Logger logger = (Logger) LoggerFactory.getLogger(AnimationPanel.class);

    /**
     * -------------window size variable---------------
     */
    private int bound_width = 800; //The size of paint
    private int bound_height = 600;
    
    ControlCenterServices controlCenterServices;

    /**
     * -------------internal variable---------------
     */
   
    private BufferedImage background_image_level_1;
    private BufferedImage obstacle_image_level_2;
    private BufferedImage enemy_uav_image_level_4;
    private BufferedImage fog_of_war_image_level_5;
    private BufferedImage uav_history_path_image_level_6;
    private BufferedImage uav_planned_tree_image_level_7;
    private BufferedImage uav_planned_path_image_level_8;
    private BufferedImage uav_image_level_10;
    private BufferedImage highlight_obstacle_image_level_3;
    private BufferedImage threat_image_level_9;

    /**
     * Draw various shapes in the simulation
     */
    private Color transparent_color;
    private Graphics2D fog_of_war_graphics;
    private Graphics2D uav_image_graphics;
    private Graphics2D obstacle_image_graphics;
    private Graphics2D enemy_uav_image_graphics;
    private Graphics2D uav_history_path_image_graphics;
    private Graphics2D uav_planned_tree_image_graphics;
    private Graphics2D uav_planned_path_image_graphics;
    private Graphics2D highlight_obstacle_image_graphics;
    private Graphics2D threat_image_graphics;

    private MyGraphic virtualizer;
    
    public static int highlight_uav_index = -1;
    public static int highlight_obstacle_index = -1;
    public static int highlight_threat_index = -1;

    private long simulation_time_in_milli_seconds = 0;
    private static int simulation_time_step = 0;
    private ArrayList<Threat> threats_from_world_view;


    public AnimationPanel(ControlCenterServices ccs) {
        this.controlCenterServices = ccs;
        initComponents();        
    }

    private void initComponents() {
       try {
            transparent_color = GraphicConfig.transparent_color;
            Color fog_of_war_color = GraphicConfig.fog_of_war_color;//Color.black;

            //initiate background image
            background_image_level_1 = ImageUtil.retrieveImage("/oakland.png");

            //initiate obstacle image
            obstacle_image_level_2 = createBufferedImage();
            obstacle_image_graphics = obstacle_image_level_2.createGraphics();

            highlight_obstacle_image_level_3 = createBufferedImage();
            highlight_obstacle_image_graphics = highlight_obstacle_image_level_3.createGraphics();

            //initiate enemy_uav_image
            enemy_uav_image_level_4 = createBufferedImage();
            enemy_uav_image_graphics = enemy_uav_image_level_4.createGraphics();

            threat_image_level_9 = createBufferedImage();
            threat_image_graphics = threat_image_level_9.createGraphics();

            //initiate fog_of_war image
            fog_of_war_image_level_5 = createBufferedImage();
            fog_of_war_graphics = fog_of_war_image_level_5.createGraphics();
            fog_of_war_graphics.setBackground(fog_of_war_color);
            fog_of_war_graphics.setColor(fog_of_war_color);
            fog_of_war_graphics.fillRect(0, 0, bound_width, bound_height);

            //initiate uav_image
            uav_image_level_10 = createBufferedImage();
            uav_image_graphics = uav_image_level_10.createGraphics();
            uav_image_graphics.setBackground(transparent_color);

            //initiate history path image to store history path of uavs
            uav_history_path_image_level_6 = createBufferedImage();
            uav_history_path_image_graphics = uav_history_path_image_level_6.createGraphics();
            uav_history_path_image_graphics.setBackground(transparent_color);

            //initiate planned tree image to store planned tree of uavs
            uav_planned_tree_image_level_7 = createBufferedImage();
            uav_planned_tree_image_graphics = uav_planned_tree_image_level_7.createGraphics();
            uav_planned_tree_image_graphics.setBackground(transparent_color);

            //initiate planned path image to store planned paths of uavs
            uav_planned_path_image_level_8 = createBufferedImage();
            uav_planned_path_image_graphics = uav_planned_path_image_level_8.createGraphics();
            uav_planned_path_image_graphics.setBackground(transparent_color);

            //initate mygraphic for drawing various simulation components
            virtualizer = new MyGraphic();
            
            threats_from_world_view = controlCenterServices.getThreats();
                        
            //draw obstacles
            //this.initObstaclesInObstacleImage(init_config.getObstacles());
            
            //initiate parameters according to the world
            //this.initParameterFromInitConfig(world);
            
            //Covers areas yet to be scouted by the scout UAV
            //this.initFogOfWarImage();

        } catch (/*IO*/Exception ex) {
            logger.error("{}",ex.toString());
        }
    }
    
    /** start the threat to drive the world and paint the graph. The thread is implemented by AnimatorListener Class in this file.
     * 
     */
    public void start() { 
        System.out.println("starting animation listener");
        Timer timer = new Timer(500, new AnimatorListener(this));
        timer.start();
    }

    private BufferedImage createBufferedImage() {
        return new BufferedImage(CC_StaticInitConfig.bound_width, CC_StaticInitConfig.bound_height,
                                    BufferedImage.TYPE_INT_ARGB);
    }
    
    /** clear the images, which is dynamically updated.
     * 
     */
    public void clearUAVImageBeforeUpdate() {
        clearImageBeforeUpdate(uav_image_graphics);
        clearImageBeforeUpdate(enemy_uav_image_graphics);
        clearImageBeforeUpdate(highlight_obstacle_image_graphics);
        clearImageBeforeUpdate(threat_image_graphics);
    }
    
    private void clearImageBeforeUpdate(Graphics2D graphics) {
        graphics.setColor(transparent_color);
        graphics.setBackground(transparent_color);
        graphics.clearRect(0, 0, bound_width, bound_height);
    }
    
    /** update the images at each time step.
     * 
     */
    public void updateImageAtEachIteration() {
        updateThreatImage();
        updateObstacleImage(controlCenterServices.getObstacles());
    }
    
    /** update graphics of the highlighted(chosen) obstacle.
     * 
     * @param obstacles 
     */
    private void updateObstacleImage(ArrayList<Obstacle> obstacles) {
        for (Obstacle obs : obstacles) {
            virtualizer.drawObstacle(obstacle_image_graphics, obs, GraphicConfig.obstacle_center_color, GraphicConfig.obstacle_edge_color, null);
            virtualizer.highlightObstacle(highlight_obstacle_image_graphics, obs,
                        GraphicConfig.obstacle_center_color, GraphicConfig.obstacle_edge_color, GraphicConfig.highlight_obstacle_color);
        }
    }
    
    /** repaint the threat image.
     * 
     */
    private void updateThreatImage() {
        ArrayList<Threat> threats = controlCenterServices.getThreats();
        if(threats != null)
        {
            for (Threat threat : threats) 
            {
                if (!threat.isEnabled()) {
                    continue;
                }
                if (threat.getIndex() == AnimationPanel.highlight_threat_index) {
                    virtualizer.drawThreat(threat_image_graphics, threat, GraphicConfig.threat_color, GraphicConfig.highlight_threat_color);
                } else {
                    virtualizer.drawThreat(threat_image_graphics, threat, GraphicConfig.threat_color, null);
                }
//                int threat_index = threat.getIndex();
//                if (this.threats_from_world_view.get(threat_index).getMode() == Threat.LOCKED_MODE) {
//                    virtualizer.drawCombatSymbol(threat_image_graphics, threat.getCoordinates(), Threat.threat_width * 3 / 2, Color.red);
//                }
            }
        }
    }
   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background_image_level_1, 0, 0, null);
        g.drawImage(obstacle_image_level_2, 0, 0, null);
        g.drawImage(this.highlight_obstacle_image_level_3, 0, 0, null);
        g.drawImage(this.threat_image_level_9, 0, 0, null);
        g.drawImage(enemy_uav_image_level_4, 0, 0, null);
        if (CC_StaticInitConfig.SHOW_FOG_OF_WAR) {
            g.drawImage(fog_of_war_image_level_5, 0, 0, null);
        }
        if (CC_StaticInitConfig.SHOW_HISTORY_PATH) {
            g.drawImage(uav_history_path_image_level_6, 0, 0, null);
        }
        if (CC_StaticInitConfig.SHOW_PLANNED_TREE) {
            g.drawImage(uav_planned_tree_image_level_7, 0, 0, null);
        }
        g.drawImage(uav_planned_path_image_level_8, 0, 0, null);
        g.drawImage(uav_image_level_10, 0, 0, null);
    }
}
