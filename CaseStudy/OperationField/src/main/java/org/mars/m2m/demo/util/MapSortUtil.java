/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.util;

import ch.qos.logback.classic.Logger;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author boluo
 */
public class MapSortUtil<T> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(MapSortUtil.class);

    public  ArrayList<T> sortMap(Map<T, Double> input) {
        List<Map.Entry<T, Double>> input_entry_list = new ArrayList<Map.Entry<T, Double>>(input.entrySet());
        ArrayList<T> sorted_obj=new ArrayList<T>();
        
        Collections.sort(input_entry_list, new Comparator<Map.Entry<T, Double>>() {
            @Override
            public int compare(Map.Entry<T, Double> entry_1, Map.Entry<T, Double> entry_2) {
                return entry_1.getValue().compareTo(entry_2.getValue());
            }
        });
        
        int entry_size=input_entry_list.size();
        for(int i=0;i<entry_size;i++)
        {
            T key_obj=input_entry_list.get(i).getKey();
            sorted_obj.add(key_obj);
        }
        return sorted_obj;
    }
    
    public static void main(String[] args)
    {
        Map<String, Double> test_map=new HashMap<String, Double>();
        test_map.put("Str3", 3.0);
        test_map.put("Str2", 2.0);
        test_map.put("Str1", 1.0);
        MapSortUtil sort_map=new MapSortUtil<String>();
        ArrayList<String> results=sort_map.sortMap((Map<String, Double>) test_map);
        for(String result:results)
        {
            logger.debug(result);
        }
    }
}
