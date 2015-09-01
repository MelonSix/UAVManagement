/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.util;

import ch.qos.logback.classic.Logger;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.mars.m2m.demo.algorithm.RRT.RRTNode;
import org.slf4j.LoggerFactory;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.Conflict;
import org.mars.m2m.demo.model.shape.Point;
import org.mars.m2m.demo.model.shape.Trajectory;

/**
 *
 * @author Yulin_Zhang
 */
public class ConflictCheckUtil {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ConflictCheckUtil.class);

    /**if conflicted then return true, otherwise return false;
     * 
     * @param obstacles
     * @param threats
     * @param coordinate_x
     * @param coordinate_y
     * @return
     */
    public static boolean checkPointInObstacles(ArrayList<Obstacle> obstacles, float coordinate_x, float coordinate_y) {
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                
                Rectangle bound=null;
                try{
                 bound= obstacle.getShape().getBounds();
                }catch(Exception e)
                {
                    logger.debug("error index {}",obstacle.getIndex());
                    logger.error(e.toString());
                }
                //increase a little bit obs_mbr to keep a visible safe distance from obstacle and make it looks less dangerous.
                bound.setBounds(bound.x - 2, bound.y - 2, bound.width + 4, bound.height + 4);
               
                if (bound.contains(coordinate_x, coordinate_y)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**if conflicted then return true, otherwise return false;
     * 
     * @param obstacles
     * @param threat
     * @return
     */
    public static boolean checkThreatInObstacles(ArrayList<Obstacle> obstacles, Rectangle threat_mbr) {
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                
                Rectangle obs_mbr=null;
                try{
                 obs_mbr= obstacle.getShape().getBounds();
                }catch(Exception e)
                {
                    logger.debug("error index"+obstacle.getIndex());
                    logger.error(e.toString());
                }
                //increase a little bit obs_mbr to keep a visible safe distance from obstacle and make it looks less dangerous.
                obs_mbr.setBounds(obs_mbr.x - 2, obs_mbr.y - 2, obs_mbr.width + 4, obs_mbr.height + 4);
               
                if (obs_mbr.intersects(threat_mbr)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /** returns true if conflicted
     * 
     * @param uav_future_path
     * @param uav_conflict
     * @param uav_safe_conflict_dist
     * @return 
     */
    public static boolean checkUAVConflict(RRTNode new_node,Conflict uav_conflict)
    {
         float uav_safe_conflict_dist=uav_conflict.getConflict_range();
        if(uav_conflict==null)
        {
            return false;
        }
        int uav_conflict_size=uav_conflict.getPath_prefound().size();
        int new_node_exptected_time_step=new_node.getExpected_time_step();
        if(new_node_exptected_time_step<uav_conflict_size)
        {
            Point conflict_point=uav_conflict.getPath_prefound().get(new_node_exptected_time_step);
            int conflict_time=conflict_point.getExptected_time_step();
            if(conflict_time==new_node_exptected_time_step&& DistanceUtil.distanceBetween(conflict_point.toFloatArray(), new_node.getCoordinate())<uav_safe_conflict_dist)
            {
                return true;
            }else if(conflict_time>new_node_exptected_time_step)
            {
                return false;
            }
        }
//        for(int i=0;i<uav_conflict_size;i++)
//        {
//            Point conflict_point=uav_conflict.getPath_prefound().get(i);
//            int conflict_time=conflict_point.getExptected_time_step();
//            
//            if(conflict_time==new_node_exptected_time_step&& DistanceUtil.distanceBetween(conflict_point.toFloatArray(), new_node.getCoordinate())<StaticInitConfig.SAFE_DISTANCE_FOR_CONFLICT)
//            {
//                return true;
//            }else if(conflict_time>new_node_exptected_time_step)
//            {
//                return false;
//            }
//        }
        return false;
    }
    
    /** return true is the trajectory is in obstacles
     * 
     * @param obstacles
     * @param traj
     * @return 
     */
    public static boolean checkTrajectoryInObstacles(ArrayList<Obstacle> obstacles, Trajectory traj)
    {
        Point[] way_points=traj.getPoints();
        for(Point point:way_points)
        {
            if(checkPointInObstacles(obstacles,(float)point.getX(),(float)point.getY()))
            {
                return true;
            }
        }
        return false;
    }
    
    /**check whether new_node in the obstacle 
     * 
     * @param obstacles
     * @param node
     * @return 
     */
    public static boolean checkNodeInObstacles(ArrayList<Obstacle> obstacles,  RRTNode node) {
        float[] coordinate = node.getCoordinate();
        return checkPointInObstacles(obstacles, coordinate[0], coordinate[1]);
    }

    /** if line is crossed with obstacles return true; otherwise return false;
     * 
     * @param obstacles
     * @param start_coord
     * @param end_coord
     * @return 
     */
    public static boolean checkLineInObstacles(ArrayList<Obstacle> obstacles, float[] start_coord, float[] end_coord) {
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                if (ShapeUtil.isIntersected(obstacle.getShape().getBounds(), start_coord, end_coord)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /** return true if two rectangle intersects
     * 
     * @param rect1
     * @param rect2
     * @return 
     */
    public static boolean checkMBRIntersected(Rectangle rect1,Rectangle rect2)
    {
        return rect1.intersects(rect2);
    }
    
    public static void main(String[] args)
    {

    }
}
