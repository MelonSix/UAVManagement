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
package org.mars.m2m.demo.controlcenter.services;

import java.util.ArrayList;
import org.mars.m2m.demo.controlcenter.appConfig.StaticInitConfig;
import org.mars.m2m.demo.controlcenter.model.Conflict;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Threat;

/**
 *
 * @author Yulin_Zhang
 */
public abstract class KnowledgeInterface
{

    protected final String rootNode = StaticInitConfig.UAV_KNOWLEDGE;
    protected final String firstChild = StaticInitConfig.OBSTACLE_INFO;
    protected final String secondChild = StaticInitConfig.THREAT_INFO;
    protected final String thirdChild = StaticInitConfig.CONFLICT_INFO;

    protected int obstacle_num;
    protected int threat_num;
    protected int conflict_num;

    public KnowledgeInterface() {
    }
    
    public abstract boolean removeObstacle(Obstacle obstacle);

    public abstract boolean removeThreat(Threat threat);

    public abstract boolean removeConflict(Conflict conflict);

    public abstract ArrayList<Obstacle> getObstacles();

    public abstract void setObstacles(ArrayList<Obstacle> obstacles);

    public abstract ArrayList<Threat> getThreats();

    public abstract void setThreats(ArrayList<Threat> threats);

    public abstract ArrayList<Conflict> getConflicts();

    public abstract void setConflicts(ArrayList<Conflict> conflicts);

    public abstract void addConflict(Conflict conflict);

    public abstract void addThreat(Threat threat);

    public abstract boolean containsObstacle(Obstacle obstacle);

    public abstract boolean containsThreat(Threat threat);

    public abstract boolean containsConflict(Conflict conflict);

    public abstract void addObstacle(Obstacle obstacle);
}
