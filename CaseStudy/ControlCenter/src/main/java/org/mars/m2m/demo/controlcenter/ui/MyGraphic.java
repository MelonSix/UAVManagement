/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Threat;

/**
 *
 * @author Yulin_Zhang
 */
public class MyGraphic {

    private static int uav_base_line_width = 3;

public void drawThreat(Graphics2D graphics, Threat threat, Color target_color, Color target_highlight_color) {
        graphics.setComposite(AlphaComposite.SrcOver);
        if (target_highlight_color != null) {
            graphics.setColor(target_highlight_color);
        } else {
            graphics.setColor(target_color);
        }
        graphics.setStroke(new BasicStroke(3.0f));//Set the width of the stroke
        graphics.drawString(CC_StaticInitConfig.THREAT_NAME + threat.getIndex(), threat.getCoordinates()[0] - 10, threat.getCoordinates()[1] - 15);
        int[] upper_left_point = new int[2];
        upper_left_point[0] = (int) threat.getCoordinates()[0] - Threat.threat_width / 2;
        upper_left_point[1] = (int) threat.getCoordinates()[1] - Threat.threat_height / 2;
        this.drawTankTarget(graphics, upper_left_point, Threat.threat_width, Threat.threat_height);
    }

public void drawTankTarget(Graphics2D graphics, int[] upper_left_point, int width, int height) {
        graphics.drawRect(upper_left_point[0], upper_left_point[1], width, height);
        int oval_width = width * 3 / 4;
        int oval_height = height / 2;
        graphics.drawOval(upper_left_point[0] + width / 2 - oval_width / 2, upper_left_point[1] + height / 2 - oval_height / 2, oval_width, oval_height);
    }

public void highlightObstacle(Graphics2D graphics, Obstacle obstacle, Color obstacle_center_color, Color obstacle_edge_color, Color obstacle_hightlight_color) {
        graphics.setComposite(AlphaComposite.SrcOver);
        graphics.setStroke(new BasicStroke(2f));
        if (obstacle_hightlight_color != null) {
            graphics.setColor(obstacle_hightlight_color);
        } else {
            graphics.setColor(obstacle_edge_color);
        }
        graphics.draw(obstacle.getShape());
    }

public void drawCombatSymbol(Graphics2D graphics, float[] combat_center, int combat_cross_len, Color combat_color) {
        graphics.setComposite(AlphaComposite.SrcOver);
        graphics.setColor(combat_color);
        double angle = Math.PI / 4;
        double[] upper_left_coord = new double[2];
        double[] upper_right_coord = new double[2];
        double[] lower_left_coord = new double[2];
        double[] lower_right_coord = new double[2];

        upper_left_coord[0] = combat_center[0] - Math.cos(angle) * combat_cross_len / 2;
        upper_left_coord[1] = combat_center[1] - Math.sin(angle) * combat_cross_len / 2;

        upper_right_coord[0] = combat_center[0] + Math.cos(angle) * combat_cross_len / 2;
        upper_right_coord[1] = combat_center[1] - Math.sin(angle) * combat_cross_len / 2;

        lower_left_coord[0] = combat_center[0] - Math.cos(angle) * combat_cross_len / 2;
        lower_left_coord[1] = combat_center[1] + Math.sin(angle) * combat_cross_len / 2;

        lower_right_coord[0] = combat_center[0] + Math.cos(angle) * combat_cross_len / 2;
        lower_right_coord[1] = combat_center[1] + Math.sin(angle) * combat_cross_len / 2;

        graphics.drawLine((int) upper_left_coord[0], (int) upper_left_coord[1], (int) lower_right_coord[0], (int) lower_right_coord[1]);
        graphics.drawLine((int) upper_right_coord[0], (int) upper_right_coord[1], (int) lower_left_coord[0], (int) lower_left_coord[1]);
    }
    
public void drawObstacle(Graphics2D graphics, Obstacle obstacle, Color obstacle_center_color, Color obstacle_edge_color, Color obstacle_hightlight_color) {
        graphics.setComposite(AlphaComposite.SrcOver);
        graphics.setStroke(new BasicStroke(1f)); //Set the width of the stroke
        graphics.setColor(obstacle_center_color);
        graphics.fill(obstacle.getShape());
        if (obstacle_hightlight_color != null) {
            graphics.setColor(obstacle_hightlight_color);
        } else {
            graphics.setColor(obstacle_edge_color);
        }
        graphics.draw(obstacle.getShape());
    }
}
