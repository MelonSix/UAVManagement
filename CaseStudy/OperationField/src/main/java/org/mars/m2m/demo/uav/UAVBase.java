/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import org.mars.m2m.demo.config.OpStaticInitConfig;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mars.m2m.demo.util.ImageUtil;
import org.mars.m2m.demo.model.shape.Circle;

/** This is the base of all uavs.
 *
 * @author boluo
 */
public class UAVBase {

    private float[] coordinate;
    private int base_radius;
    private BufferedImage image;
    private Circle base_shape;
    private Map<Integer, float[]> uav_port_map;
    public UAVBase(float[] coordinate, int base_radius) {
        try {
            this.coordinate = coordinate;
            this.base_radius = base_radius;
            this.base_shape = new Circle(coordinate[0], coordinate[1], this.base_radius);
            image = ImageUtil.retrieveImage("/radar2.jpg");
        } catch (IOException ex) {
            Logger.getLogger(UAVBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        initUAVPort();
    }

    /** It initiate the places for all attackers.
     * 
     */
    public final void initUAVPort() {
        uav_port_map = new HashMap<>();
            int port_radius = 0;
            double theta = 0;
            for (int uav_index = 1; uav_index <= OpStaticInitConfig.ATTACKER_NUM; uav_index++)
            {
                double delta_theta = Math.PI * 2 / uav_index;
                float[] coord = new float[2];
                int ran_port_radius = (int)(1+(int)(Math.random()*49));
                coord[0] = this.coordinate[0] + (float) ( ran_port_radius * Math.cos(theta));
                coord[1] = this.coordinate[1] + (float) ( ran_port_radius * Math.sin(theta));
//                coord[0] = this.coordinate[0] + (float) (port_radius * Math.cos(theta));
//                coord[1] = this.coordinate[1] + (float) (port_radius * Math.sin(theta));
                uav_port_map.put(uav_index, coord);
                theta += delta_theta;
            }
    }

    public float[] assignUAVLocation(int attacker_index) {
            return uav_port_map.get(attacker_index);
    }

    public float[] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(float[] coordinate) {
        this.coordinate = coordinate;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Circle getBase_shape() {
        return base_shape;
    }

}
