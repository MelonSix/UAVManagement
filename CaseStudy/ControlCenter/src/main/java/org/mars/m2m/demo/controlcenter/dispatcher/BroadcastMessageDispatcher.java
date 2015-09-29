/* 
 * Copyright (c) Yulin Zhang
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mars.m2m.demo.controlcenter.dispatcher;

import java.awt.Rectangle;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.Conflict;
import org.mars.m2m.demo.controlcenter.model.KnowledgeAwareInterface;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Target;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.util.AttackerUtils;
import org.mars.m2m.demo.controlcenter.util.RectangleUtil;


/** This is a implementation for broadcast information sharing. It shares all the information the unit knows to all attackers.
 *
 * @author Yulin_Zhang
 */
public class BroadcastMessageDispatcher extends MessageDispatcher {

    public BroadcastMessageDispatcher(KnowledgeAwareInterface intelligent_unit) {
        super(intelligent_unit);
    }

    @Override
    public void decideAndSumitMsgToSend() 
    {
        List<Obstacle> obstacles = intelligent_unit.getObstacles();
        int obstacle_num = obstacles.size();
        List<Threat> threats = intelligent_unit.getThreats();
        int threat_num = threats.size();
        List<Conflict> conflicts = intelligent_unit.getConflicts();
        int conflict_num = conflicts.size();

        int attacker_num = HandleTree.attackersNode.getChildCount();
        for (int i = 0; i < attacker_num; i++) 
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) HandleTree.attackersNode.getChildAt(i).getChildAt(0);
            
            if(node != null)
            {
                AttackerModel attacker = AttackerUtils.getVirtualizedAttacker(node);
                
                Rectangle attacker_rect = null;
                Target attacker_target=attacker.getTarget_indicated_by_role();
                if ( attacker_target!= null) {
                    attacker_rect = RectangleUtil.findMBRRect(attacker.getCenterCoordinates(), attacker_target.getCoordinates());
                }

                for (int j = 0; j < obstacle_num; j++) {
                    Obstacle obstacle = obstacles.get(j);
                    if (!attacker.containsObstacle(obstacle)) {
                        super.addRecvMessage(attacker.getIndex(), obstacle);
                    }
                }

                for (int j = 0; j < threat_num; j++) {
                    Threat threat = threats.get(j);
    //                if (!kb.containsThreat(threat)) {
                        this.addRecvMessage(attacker.getIndex(), threat);
    //                }
                }

                if(attacker_rect==null)
                {
                    continue;
                }

                for (int j = 0; j < conflict_num; j++) 
                {
                    DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) HandleTree.attackersNode.getChildAt(j).getChildAt(0);
                    
                    if (node2 != null)
                    {
                        AttackerModel conflict_uav = AttackerUtils.getVirtualizedAttacker(node2);
                        Conflict conflict = conflicts.get(j);
                        int uav_index = conflict.getUav_index();
                        Target conflict_uav_target = conflict_uav.getTarget_indicated_by_role();
                        if (conflict_uav_target != null && uav_index != attacker.getIndex()) {
                            Rectangle conflict_uav_rect = RectangleUtil.findMBRRect(conflict_uav.getCenterCoordinates(), conflict_uav_target.getCoordinates());
                            if (attacker_rect.intersects(conflict_uav_rect)) {
                                super.addRecvMessage(attacker.getIndex(), conflict);
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void register(Integer uav_index, float[] current_loc, Target target) {
    }

}
