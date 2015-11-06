/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.ui;

import ch.qos.logback.classic.Logger;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.mars.m2m.demo.config.GraphicConfig;
import org.mars.m2m.demo.config.NonStaticInitConfig;
import org.mars.m2m.demo.config.OpStaticInitConfig;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.Threat;
import org.mars.m2m.demo.uav.Attacker;
import org.mars.m2m.demo.uav.Scout;
import org.mars.m2m.demo.uav.UAV;
import org.mars.m2m.demo.uav.UAVBase;
import org.mars.m2m.demo.util.DistanceUtil;
import org.mars.m2m.demo.util.ImageUtil;
import org.mars.m2m.demo.util.MyPopupMenu;
import org.mars.m2m.demo.world.AnimatorListener;
import org.mars.m2m.demo.world.World;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AnimationPanel extends JPanel implements MouseListener
{
    private static Logger logger = (Logger) LoggerFactory.getLogger(AnimationPanel.class);

    /**
     * -------------window size variable---------------
     */
    private int bound_width; //The size of paint
    private int bound_height;

    /**
     * -------------internal variable---------------
     */
    private World world;
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
//
//    private static ArrayList<Attacker> attackers;
//    private ArrayList<Threat> threats_from_god_view;
//    private ArrayList<Scout> scouts;
    private UAVBase uav_base;
//    private ControlCenter control_center;

    public static int highlight_uav_index = -1;
    public static int highlight_obstacle_index = -1;
    public static int highlight_threat_index = -1;

    private long simulation_time_in_milli_seconds = 0;
    private static int simulation_time_step = 0;
    private ArrayList<Scout> scouts;
    private ArrayList<Attacker> attackers;
    private ArrayList<Threat> threats_from_world_view;
    private MyPopupMenu my_popup_menu;

    public AnimationPanel() {
//        initComponents();
    }

    public void initComponents() {
       try {
           bound_height = 980;
           bound_width = 1850;
            transparent_color = GraphicConfig.transparent_color;
            Color fog_of_war_color = GraphicConfig.fog_of_war_color;//Color.black;

            //initiate background image
            background_image_level_1 = ImageUtil.retrieveImage("/map.jpg");

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
            

           //initiate world
            NonStaticInitConfig init_config = new NonStaticInitConfig(bound_width, bound_height);
            this.world = new World(init_config);            
            this.threats_from_world_view = world.getThreats();
            
            //draw obstacles
            this.initObstaclesInObstacleImage(init_config.getObstacles());
            
            //initiate parameters according to the world
            this.initParameterFromInitConfig(world);
            
            //Covers areas yet to be scouted by the scout UAV
            this.initFogOfWarImage();
            
            my_popup_menu = new MyPopupMenu(init_config.getThreats());
            this.addMouseListener(this);

        } catch (/*IO*/Exception ex) {
            logger.error("{}",ex.toString());
        }
    }
    
    /** start the threat to drive the world and paint the graph. The thread is implemented by AnimatorListener Class in this file.
     * 
     */
    public void start() {
        
        //drive the world and ui
        OpStaticInitConfig.SIMULATION_WITH_UI_TIMER = 
             new javax.swing.Timer(
                     (int) (OpStaticInitConfig.INIT_SIMULATION_DELAY / OpStaticInitConfig.SIMULATION_SPEED),
                     new AnimatorListener(this));
        OpStaticInitConfig.SIMULATION_WITH_UI_TIMER.start();
    }
    
    /**Initiate parameter from Config
     *
     * @param world
     */
    private void initParameterFromInitConfig(World world) {
        this.bound_width = world.getBound_width();
        this.bound_height = world.getBound_height();

        this.scouts = world.getScouts();
        this.attackers = world.getAttackers();
        this.uav_base = world.getUav_base();
    }

    private BufferedImage createBufferedImage() {
        return new BufferedImage(bound_width, bound_height,
                BufferedImage.TYPE_INT_ARGB);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background_image_level_1, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(obstacle_image_level_2, 0, 0, null);
        g.drawImage(this.highlight_obstacle_image_level_3, 0, 0, null);
        g.drawImage(this.threat_image_level_9, 0, 0, null);
        g.drawImage(enemy_uav_image_level_4, 0, 0, null);
        if (OpStaticInitConfig.SHOW_FOG_OF_WAR) {
            g.drawImage(fog_of_war_image_level_5, 0, 0, null);
        }
        if (OpStaticInitConfig.SHOW_HISTORY_PATH) {
            g.drawImage(uav_history_path_image_level_6, 0, 0, null);
        }
        if (OpStaticInitConfig.SHOW_PLANNED_TREE) {
            g.drawImage(uav_planned_tree_image_level_7, 0, 0, null);
        }
        g.drawImage(uav_planned_path_image_level_8, 0, 0, null);
        g.drawImage(uav_image_level_10, 0, 0, null);
    }

    private void initFogOfWarImage() {
        virtualizer.drawUAVBaseInFogOfWar(fog_of_war_graphics, this.uav_base);
    }
    
    /** paint the obstacles.
     * 
     * @param obstacles 
     */
    private void initObstaclesInObstacleImage(ArrayList<Obstacle> obstacles) {
        for (Obstacle obs : obstacles) {
            virtualizer.drawObstacle(obstacle_image_graphics, obs, GraphicConfig.obstacle_center_color, GraphicConfig.obstacle_edge_color, null);
        }
    }

    /** update graphics of the highlighted(chosen) obstacle.
     * 
     * @param obstacles 
     */
    private void updateHighlightObstacleImage(ArrayList<Obstacle> obstacles) {
        for (Obstacle obs : obstacles) {
            if (obs.getIndex() == AnimationPanel.highlight_obstacle_index) {
                virtualizer.highlightObstacle(highlight_obstacle_image_graphics, obs, GraphicConfig.obstacle_center_color, GraphicConfig.obstacle_edge_color, GraphicConfig.highlight_obstacle_color);
            }
        }
    }
    
    /** clear the given image.
     * 
     */
    private void clearImageBeforeUpdate(Graphics2D graphics) {
        graphics.setColor(transparent_color);
        graphics.setBackground(transparent_color);
        graphics.clearRect(0, 0, bound_width, bound_height);
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
    
    /** update the images at each time step.
     * 
     */
    public void updateImageAtEachIteration() {
        initUAVBase(uav_image_graphics);
//        updateTargetInUAVImageLevel(world.getThreatsForUIRendering());
        updateThreatImage();
        updateHighlightObstacleImage(world.getObstacles());
        updateUAVImage();
        updateFogOfWarImage();
        updateUAVHistoryPath();
        showUAVPlannedPath();
//        showUAVPlannedTree();
    }
    
    /** paint the uav base
     *
     * @param uav_image_graphics
     */
    private void initUAVBase(Graphics2D uav_image_graphics) {
        virtualizer.drawUAVBase(uav_image_graphics, uav_base);
    }
    
    /** repaint the threat image.
     * 
     */
    private void updateThreatImage() {
        ArrayList<Threat> threats = world.getThreats();
        for (Threat threat : threats) {
            if (!threat.isEnabled()) {
                continue;
            }
            if (threat.getIndex() == AnimationPanel.highlight_threat_index) {
                virtualizer.drawThreat(threat_image_graphics, threat, GraphicConfig.threat_color, GraphicConfig.highlight_threat_color);
            } else {
                virtualizer.drawThreat(threat_image_graphics, threat, GraphicConfig.threat_color, null);
            }
            int threat_index = threat.getIndex();
            if (this.threats_from_world_view.get(threat_index).getMode() == Threat.LOCKED_MODE) {
                virtualizer.drawCombatSymbol(threat_image_graphics, threat.getCoordinates(), Threat.threat_width * 3 / 2, Color.red);
            }
        }
    }
    
    /** repaint the uav image.
     * 
     */
    private void updateUAVImage() {
        for (Scout scout : this.scouts) {
            virtualizer.drawUAVInUAVImage(uav_image_graphics, this.uav_base, scout, null);
        }
        for (Attacker attacker : this.attackers) {
            if (attacker.getIndex() == this.highlight_uav_index) {
                virtualizer.drawUAVInUAVImage(uav_image_graphics, this.uav_base, attacker, GraphicConfig.highlight_uav_color);
            }
            virtualizer.drawUAVInUAVImage(uav_image_graphics, this.uav_base, attacker, null);

        }
    }
    
    /** repaint the fog of war image.
     * 
     */
    private void updateFogOfWarImage() 
    {
        if (!OpStaticInitConfig.SHOW_FOG_OF_WAR) {
            return;
        }
        for (Attacker attacker : this.attackers) {
            virtualizer.drawUAVInFogOfWarInLevel3(fog_of_war_graphics, (UAV) attacker);
        }
        for (Scout scout : this.scouts) {
            virtualizer.drawUAVInFogOfWarInLevel3(fog_of_war_graphics, (UAV) scout);
        }
        ArrayList<Obstacle> obstacles = this.world.getReconnaissance().getObstacles();
        for (Obstacle obstacle : obstacles) {
            virtualizer.showObstacleInFogOfWar(fog_of_war_graphics, obstacle);
        }
        ArrayList<Threat> threats = this.world.getReconnaissance().getThreats();
        for (Threat threat : threats) {
            virtualizer.showThreatInFogOfWar(fog_of_war_graphics, threat);
        }
    }
    
    /** repaint the history path of the uav
     * 
     */
    private void updateUAVHistoryPath() {
        if (!OpStaticInitConfig.SHOW_HISTORY_PATH) {
            return;
        }
        for (Attacker attacker : this.attackers) {
            if (attacker.isVisible() && attacker.getTarget_indicated_by_role() != null) {
                virtualizer.drawUAVHistoryPath(uav_history_path_image_graphics, attacker, GraphicConfig.side_a_center_color);
            }
        }
    }
    
    /** repaint the path planned by the uav.
     * 
     */
    private void showUAVPlannedPath() 
    {
        uav_planned_path_image_graphics.setBackground(transparent_color);
        uav_planned_path_image_graphics.setColor(transparent_color);
        uav_planned_path_image_graphics.clearRect(0, 0, bound_width, bound_height);
        if (!OpStaticInitConfig.SHOW_PLANNED_PATH && AnimationPanel.highlight_uav_index == -1) {
            return;
        } else if (!OpStaticInitConfig.SHOW_PLANNED_PATH && AnimationPanel.highlight_uav_index > 0) {
            for (Attacker attacker : this.attackers) {
                if (attacker.getIndex() == AnimationPanel.highlight_uav_index) {
                    virtualizer.drawUAVPlannedPath(uav_planned_path_image_graphics, attacker, GraphicConfig.uav_planned_path_color);
                }
            }
            return;
        }
        for (Attacker attacker : this.attackers) {
            if (attacker.getTarget_indicated_by_role() != null) {
                virtualizer.drawUAVPlannedPath(uav_planned_path_image_graphics, attacker, GraphicConfig.uav_planned_path_color);
            }
        }
    }
    
    
    /**************** ACCESSORS AND MUTATORS ********/    

    public long getSimulation_time_in_milli_seconds() {
        return simulation_time_in_milli_seconds;
    }

    public int getSimulation_time_step() {
        return simulation_time_step;
    }

    public void setSimulation_time_step(int simulation_time_step) {
        this.simulation_time_step = simulation_time_step;
    }

    public World getWorld() {
        return world;
    }

    /** implement mouse left click action, and highlight the chosen attacker.
     * 
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        OpStaticInitConfig.SIMULATION_ON = true;
        int chosen_attacker_index = findChosenAttacker(e.getPoint());
        AnimationPanel.setHighlightUAV(chosen_attacker_index);
    }

    /** find the attacker chosen by the user.
     * 
     * @param mouse_point
     * @return 
     */
    private int findChosenAttacker(Point mouse_point) {
        float[] mouse_point_coord = new float[]{(float) mouse_point.getX(), (float) mouse_point.getY()};
        for (Attacker attacker : attackers) {
            float[] center_coord = attacker.getCenter_coordinates();
            float dist = DistanceUtil.distanceBetween(center_coord, mouse_point_coord);
            if (dist < attacker.getUav_radar().getRadius()) {
                return attacker.getIndex();
            }
        }
        return -1;
    }

    /** implement right mouse click action, and pop up the menu for changing threat
     * 
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        OpStaticInitConfig.SIMULATION_ON = false;
        if (e.getButton() == MouseEvent.BUTTON3) {
            int chosen_attacker_index = findChosenAttacker(e.getPoint());
            if (chosen_attacker_index == -1) {
                return;
            }
            AnimationPanel.setHighlightUAV(chosen_attacker_index);
            my_popup_menu.setChoosedAttackerIndex(chosen_attacker_index);
            my_popup_menu.show(this, e.getX(), e.getY());
            OpStaticInitConfig.SIMULATION_ON = false;
        }
        OpStaticInitConfig.SIMULATION_ON = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    /** highlight the chosen uav.
     * 
     * @param uav_index 
     */
    public static void setHighlightUAV(int uav_index) {
        AnimationPanel.highlight_uav_index = uav_index;
        Attacker highlight_uav = null;
        for (Attacker attacker : World.attackers) {
            if (attacker.getIndex() == uav_index) {
                highlight_uav = attacker;
            }
        }
        if (highlight_uav != null) {
            RightControlPanel.setWorldKnowledge(highlight_uav.getKb());
        }
    }
}
