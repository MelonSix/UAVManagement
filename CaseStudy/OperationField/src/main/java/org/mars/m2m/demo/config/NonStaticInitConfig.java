/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.config;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import org.mars.m2m.demo.enums.ThreatType;
import org.mars.m2m.demo.uav.UAVBase;
import org.mars.m2m.demo.util.ConflictCheckUtil;
import org.mars.m2m.demo.util.ObtacleUtil;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.Threat;
import org.mars.m2m.demo.world.OntologyBasedKnowledge;
import org.mars.m2m.demo.world.Reconnaissance;

/**
 *
 * @author boluo
 */
public class NonStaticInitConfig {

    private int enemy_num; //The number of enemy uavs
    private int threat_num; //The number of enemy threats
    private int attacker_num; //The number of our attackers
    private int scout_num; //The number of our scouts
    
    private int bound_width;
    private int bound_height;
    private int size_of_subteam = OpStaticInitConfig.SIZE_OF_SUBTEAM;

    private ArrayList<Obstacle> obstacles;
    private ArrayList<Threat> threats;
    
    private Reconnaissance reconnaissance;

    private UAVBase uav_base;
    public static int inforshare_algorithm = OpStaticInitConfig.BROADCAST_INFOSHARE;

    //robot coordinates, robot_coordinates[1][0], robot_coordinates[1][1] represents the x, y coordinate of robot 1
    private float attacker_patrol_range = 100;
    private float threat_radius = 100;
    
    public static int threat_range_from_obstacles=30;

    public static int obstacle_num = 17;

    public NonStaticInitConfig(int ui_width, int ui_height) {
        this.bound_height = ui_height;
        this.bound_width = ui_width;
        if (!OpStaticInitConfig.UI_PARAMETER_CONFIG) {
            this.enemy_num = 0;
            this.threat_num = 10;
            this.attacker_num = 10;
            this.scout_num = 2;
        } else {
            this.threat_num = OpStaticInitConfig.THREAT_NUM;
            this.attacker_num = OpStaticInitConfig.ATTACKER_NUM;
            this.scout_num = OpStaticInitConfig.SCOUT_NUM;
        }
        float[] coordinate = new float[]{60, 60};
        UAVBase uav_base = new UAVBase(coordinate, 60);
        this.uav_base = uav_base;
        this.reconnaissance = new Reconnaissance(new OntologyBasedKnowledge());
        initObstacles();
        initThreats();
    }
    public void initThreats() {
        threats = new ArrayList<Threat>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < threat_num; i++) {
            float coordinate_x = 0;
            float coordinate_y = 0;
            boolean found = false;
            while (!found) {
                coordinate_x = random.nextFloat() * (bound_width - 3*threat_range_from_obstacles) + 2*threat_range_from_obstacles;
                coordinate_y = random.nextFloat() * (bound_height -3*threat_range_from_obstacles) +2*threat_range_from_obstacles;
                Rectangle threat_mbr=new Rectangle((int)coordinate_x - (Threat.threat_width+threat_range_from_obstacles) / 2, (int) coordinate_y - (Threat.threat_height+threat_range_from_obstacles) / 2, Threat.threat_width+threat_range_from_obstacles, Threat.threat_height+threat_range_from_obstacles);
                found = !ConflictCheckUtil.checkThreatInObstacles(obstacles, threat_mbr)
                        &&!this.uav_base.getBase_shape().intersects(threat_mbr)
                        && coordinate_x < bound_width && coordinate_x < bound_height
                        && coordinate_y < bound_width && coordinate_y < bound_height;
            }
            Threat threat = new Threat(i, new float[]{coordinate_x, coordinate_y}, 5, getThreatType(i));
            threats.add(threat);
        }
    }
    
    /**
     * Uses modulo operation to select a threat type from the threat enumeration
     * @param threatIndex
     * @return Threat type
     */
    private ThreatType getThreatType(int threatIndex)
    {
        int selectedType = (threatIndex % (ThreatType.values().length-1));
        ThreatType type = null;
        ThreatType[] values = ThreatType.values();
        for(int i=0; i<values.length;i++)
        {
            if(values[i].getType() == selectedType)
            {
                type = values[i];
                break;
            }
        }
        return type;
    }
    
    public void initThreats1() {
        threats = new ArrayList<Threat>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < threat_num; i++) {
            float coordinate_x = 0;
            float coordinate_y = 0;
            boolean found = false;
            while (!found) {
                coordinate_x = random.nextFloat() * (bound_width - 3 * attacker_patrol_range) + attacker_patrol_range;
                coordinate_y = random.nextFloat() * (bound_height - 3 * attacker_patrol_range) + attacker_patrol_range;
                found = !ConflictCheckUtil.checkPointInObstacles(obstacles, coordinate_x, coordinate_y);
            }
            Threat threat = new Threat(i, new float[]{coordinate_x, coordinate_y}, OpStaticInitConfig.STATIC_THREAT_TYPE, getThreatType(i));
            threats.add(threat);
        }
    }

    private void initConfigurationFromParameterConfiguration() {
        this.attacker_num = OpStaticInitConfig.ATTACKER_NUM;
        this.scout_num = OpStaticInitConfig.SCOUT_NUM;
        this.enemy_num = OpStaticInitConfig.ENEMY_UAV_NUM;
        this.threat_num = OpStaticInitConfig.THREAT_NUM;
    }

    /**
     * Get file path of KML,and that file storages the polygons represented
     * obstacles
     */
    public void initObstacles() {
        if (OpStaticInitConfig.EXTERNAL_KML_FILE_PATH == null) {
            String obs_path = "/Obstacle" + NonStaticInitConfig.obstacle_num + ".kml";
            obstacles = ObtacleUtil.readObstacleFromResourceKML(obs_path); //get obstacle from kml
        } else {
            obstacles = ObtacleUtil.readObstacleFromExternalKML(OpStaticInitConfig.EXTERNAL_KML_FILE_PATH);
        }
    }

    public int getEnemy_num() {
        return enemy_num;
    }

    public void setEnemy_num(int enemy_num) {
        this.enemy_num = enemy_num;
    }

    public int getAttacker_num() {
        return attacker_num;
    }

    public void setAttacker_num(int attacker_num) {
        this.attacker_num = attacker_num;
    }

    public int getScout_num() {
        return scout_num;
    }

    public void setScout_num(int scout_num) {
        this.scout_num = scout_num;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public UAVBase getUav_base() {
        return uav_base;
    }

    public void setUav_base(UAVBase uav_base) {
        this.uav_base = uav_base;
    }

    public float getAttacker_patrol_range() {
        return attacker_patrol_range;
    }

    public void setAttacker_patrol_range(float attacker_patrol_range) {
        this.attacker_patrol_range = attacker_patrol_range;
    }

    public float getThreat_radius() {
        return threat_radius;
    }

    public void setThreat_radius(float threat_radius) {
        this.threat_radius = threat_radius;
    }

    public int getBound_width() {
        return bound_width;
    }

    public void setBound_width(int bound_width) {
        this.bound_width = bound_width;
    }

    public int getBound_height() {
        return bound_height;
    }

    public void setBound_height(int bound_height) {
        this.bound_height = bound_height;
    }

    public int getThreat_num() {
        return threat_num;
    }

    public void setThreat_num(int threat_num) {
        this.threat_num = threat_num;
    }

    public ArrayList<Threat> getThreats() {
        return threats;
    }

    public void setThreats(ArrayList<Threat> threats) {
        this.threats = threats;
    }

    public int getInforshare_algorithm() {
        return inforshare_algorithm;
    }

    public void setInforshare_algorithm(int inforshare_algorithm) {
        this.inforshare_algorithm = inforshare_algorithm;
    }

    public int getSize_of_subteam() {
        return size_of_subteam;
    } 

    public Reconnaissance getReconnaissance() {
        return reconnaissance;
    }
    
}
