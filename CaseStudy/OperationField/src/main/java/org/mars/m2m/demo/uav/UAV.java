/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import org.mars.m2m.demo.config.GraphicConfig;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.mars.m2m.demo.util.VectorUtil;
import org.mars.m2m.demo.util.DistanceUtil;
import org.mars.m2m.demo.util.RectangleUtil;
import org.mars.m2m.demo.model.Target;
import org.mars.m2m.demo.model.shape.Circle;

/** UAV is the common features for both scouts and attackers.
 *
 * @author boluo
 */
public class UAV extends Unit {

    protected Color center_color;
    protected Color radar_color; //the radar color in world
    protected Circle uav_radar;
    protected double current_angle = 0;
    protected double max_angle =0;
    protected int speed = 5;
    protected boolean visible=true;
    protected float remained_energy=2000;
        
    protected static ArrayList<Integer> occupiedPorts;
    
    public UAV(int index, Target target_indicated_by_role, int uav_type, float[] center_coordinates,float remained_energy) {
        super(index, target_indicated_by_role, uav_type, center_coordinates);
        this.remained_energy=remained_energy;
    }

    /** update the coordinate of center and radar of the scout according to its new coordinate.
     *
     * @param center_coordinate_x
     * @param center_coordinate_y
     */
    public void moveTo(float center_coordinate_x, float center_coordinate_y) {
        float moved_dist=DistanceUtil.distanceBetween(this.center_coordinates, new float[]{center_coordinate_x,center_coordinate_y});
        this.remained_energy-=moved_dist;
        uav_center.setCoordinate(center_coordinate_x, center_coordinate_y);
        uav_radar.setCoordinate(center_coordinate_x, center_coordinate_y);
        this.setCenter_coordinates(uav_radar.getCenter_coordinates());
    }

    /** extend Toward Goad considering the dynamics of the uav.
     * 
     * @param current_coordinate
     * @param current_angle
     * @param random_goal_coordinate
     * @param max_length
     * @param max_angle
     * @return 
     */
    protected float[] extendTowardGoalWithDynamics(float[] current_coordinate, double current_angle, float[] random_goal_coordinate, float max_length, double max_angle) {
        double toward_goal_angle = VectorUtil.getAngleOfVectorRelativeToXCoordinate(random_goal_coordinate[0] - current_coordinate[0], random_goal_coordinate[1] - current_coordinate[1]);
        double delta_angle = VectorUtil.getBetweenAngle(toward_goal_angle, current_angle);
        float[] new_node_coord = new float[2];
        if (delta_angle > max_angle) {
            double temp_goal_angle1 = VectorUtil.getNormalAngle(current_angle - max_angle);
            double delta_angle_1 = VectorUtil.getBetweenAngle(toward_goal_angle, temp_goal_angle1);

            double temp_goal_angle2 = VectorUtil.getNormalAngle(current_angle + max_angle);
            double delta_angle_2 = VectorUtil.getBetweenAngle(toward_goal_angle, temp_goal_angle2);

            if (delta_angle_1 < delta_angle_2) {
                toward_goal_angle = temp_goal_angle1;
            } else {
                toward_goal_angle = temp_goal_angle2;
            }
        }
        new_node_coord[0] = current_coordinate[0] + (float) (Math.cos(toward_goal_angle) * max_length);
        new_node_coord[1] = current_coordinate[1] + (float) (Math.sin(toward_goal_angle) * max_length);
        return new_node_coord;
    }

    /** check whether obstacle is in the rectangle, which is constructed by the current location of this uav and its target location.
     * 
     * @param obs_mbr
     * @return 
     */
    public boolean isObstacleInTargetMBR(Rectangle obs_mbr)
    {
        float[] target_coord=this.getTarget_indicated_by_role().getCoordinates();
        Rectangle rect = RectangleUtil.findMBRRect(this.center_coordinates, target_coord);
        if(rect.intersects(obs_mbr))
        {
            return true;
        }else{
            return false;
        }
    }
    
    public Circle getUav_radar() {
        return uav_radar;
    }

    public void setUav_radar(Circle uav_radar) {
        this.uav_radar = uav_radar;
    }

    public void initColor(int uav_index) {
        center_color = GraphicConfig.uav_colors.get(uav_index);
        radar_color = new Color(center_color.getRed(), center_color.getGreen(), center_color.getBlue(), 128);
    }

    public Color getCenter_color() {
        return center_color;
    }

    public Color getRadar_color() {
        return radar_color;
    }

    public double getCurrent_angle() {
        return current_angle;
    }

    public void setCurrent_angle(double current_angle) {
        this.current_angle = current_angle;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Gets an ephemeral port number">
    /**
     * Gets a port number within the range 49152 to 65532
     * @return Port number
     */
    protected int selectPortNumber() {
        if(occupiedPorts != null)
        {
            int portNumber = 49152 + (int) (Math.random() * 16381); //range is from 49152 -> 65532
            while (occupiedPorts.contains(portNumber)) {
                portNumber = 49152 + (int) (Math.random() * 16381);
            }
            occupiedPorts.add(portNumber);
            return portNumber;
        }
        else
            return -1;
    }
    //</editor-fold>

    
}
