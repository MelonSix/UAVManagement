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
package org.mars.m2m.demo.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.mars.m2m.demo.config.OpStaticInitConfig;
import org.mars.m2m.demo.model.Threat;
import org.mars.m2m.demo.world.World;

/**
 *
 * @author Yulin_Zhang
 */
public class MyPopupMenu extends JPopupMenu implements ActionListener {

    ArrayList<JMenuItem> items;

    /** internal variable
     * 
     */
    private int choosen_attacker_index=-1;
    
    public MyPopupMenu(ArrayList<Threat> thrts) {
        super();
        items = new ArrayList<JMenuItem>();
        
        ArrayList<Threat> threats = thrts;
        for (Threat threat : threats) {
            JMenuItem threat_item = new JMenuItem(OpStaticInitConfig.THREAT_NAME+threat.getIndex());
            items.add(threat_item);
            this.add(threat_item);
            threat_item.addActionListener(this);
        }

    }
    
    public void setChoosedAttackerIndex(int attacker_index)
    {
        this.choosen_attacker_index=attacker_index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = items.indexOf(e.getSource());
    }

}
