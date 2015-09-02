/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import org.mars.m2m.demo.config.GraphicConfig;
import org.mars.m2m.demo.config.StaticInitConfig;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import org.mars.m2m.demo.util.ConflictCheckUtil;
import org.mars.m2m.demo.util.VectorUtil;
/*import org.mars.m2m.demo.world.ControlCenter;*/
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.shape.Circle;
import org.mars.m2m.demo.world.Reconnaissance;
import org.mars.m2m.demo.world.World;

/**
 *
 * @author boluo
 */
public class Scout extends UAV {

    private float[] base_coordinate;
    private LinkedList<Float> move_at_y_coordinate_task;
    private Float current_y_coordinate_task = null;
    private int direction = 1;
    private Reconnaissance reconnaissance;
    private int conflict_avoid = 1;

    public Scout(int index, int uav_type, float[] center_coordinates, float[] base_coordinate, 
            Reconnaissance r, float remained_energy) 
    {
        super(index, null, uav_type, center_coordinates,remained_energy);
        this.uav_radar = new Circle(center_coordinates[0], center_coordinates[1], StaticInitConfig.scout_radar_radius);
        this.base_coordinate = base_coordinate;
        this.reconnaissance = r;
        this.move_at_y_coordinate_task = new LinkedList<Float>();
        this.max_angle = (float) Math.PI / 2;
        this.speed = StaticInitConfig.SPEED_OF_SCOUT;
        center_color = GraphicConfig.uav_colors.get(22);
        radar_color = new Color(center_color.getRed(), center_color.getGreen(), center_color.getBlue(), 128);
    }

    /** to update the coordinate of the scout.
     * 
     * @return 
     */
    public boolean moveToNextWaypoint() {
        if (current_y_coordinate_task == null && move_at_y_coordinate_task.size() > 0) {
            current_y_coordinate_task = move_at_y_coordinate_task.removeFirst();
        } else if(current_y_coordinate_task == null && move_at_y_coordinate_task.size()==0){
            this.setVisible(false);
            return false;
        }
        float[] next_waypoint = new float[2];
        float[] goal_waypoint = new float[2];
        goal_waypoint[1] = current_y_coordinate_task;
        ArrayList<Obstacle> obstacles = this.reconnaissance.getObstacles();
        if (direction == 1) //move to the right
        {
            goal_waypoint[0] = this.center_coordinates[0] + this.speed;
            next_waypoint = extendTowardGoalWithDynamics(this.center_coordinates, this.current_angle, goal_waypoint, this.speed, this.max_angle);
            if (next_waypoint[0] > World.bound_width) {
                next_waypoint[0] -= this.speed;
                if (move_at_y_coordinate_task.size() == 0) {
                    current_y_coordinate_task=null;
                    return false;
                }
                current_y_coordinate_task = move_at_y_coordinate_task.removeFirst();
                goal_waypoint[1] = current_y_coordinate_task;
                direction = 0;
            } else if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                next_waypoint[0] = this.center_coordinates[0];
                next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                    conflict_avoid = -1 * conflict_avoid;
                    next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                }
            }
        }

        if (direction == 0)//move to the left
        {
            goal_waypoint[0] = this.center_coordinates[0] - this.speed;
            next_waypoint = extendTowardGoalWithDynamics(this.center_coordinates, this.current_angle, goal_waypoint, this.speed, this.max_angle);
            if (next_waypoint[0] < 0) {
                next_waypoint[0] += this.speed;
                if (move_at_y_coordinate_task.size() == 0) {
                    current_y_coordinate_task=null;
                    return false;
                }
                current_y_coordinate_task = move_at_y_coordinate_task.removeFirst();
                goal_waypoint[1] = current_y_coordinate_task;
                direction = 1;
            } else if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                next_waypoint[0] = this.center_coordinates[0];
                next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                    conflict_avoid = -1 * conflict_avoid;
                    next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                }
            }
        }
        this.current_angle = VectorUtil.getAngleOfVectorRelativeToXCoordinate(next_waypoint[0] - this.center_coordinates[0], next_waypoint[1] - this.center_coordinates[1]);
        moveTo(next_waypoint[0], next_waypoint[1]);
        return true;
    }

    /** set the responsible y coordinate to be scanned by the scout. The scout will scan the line at y coordinate from one side to the other.
     * 
     * @param move_at_y_coordinate_task 
     */
    public void setMove_at_y_coordinate_task(LinkedList<Float> move_at_y_coordinate_task) {
        this.move_at_y_coordinate_task = move_at_y_coordinate_task;
    }

}
