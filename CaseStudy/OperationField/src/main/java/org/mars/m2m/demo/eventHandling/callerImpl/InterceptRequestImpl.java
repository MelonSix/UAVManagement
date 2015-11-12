/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.callerImpl;

import javax.swing.SwingUtilities;
import org.eclipse.leshan.core.request.BootstrapRequest;
import org.eclipse.leshan.core.request.DeregisterRequest;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.request.UpdateRequest;
import org.eclipse.leshan.core.request.UplinkRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.mars.m2m.apiExtension.InterceptRequest;
import org.mars.m2m.demo.ui.LogWindow;
import org.mars.m2m.dmcore.util.DmCommons;

/**
 *
 * @author AG BRIGHTER
 */
public class InterceptRequestImpl implements InterceptRequest
{

    public InterceptRequestImpl() {
    }    

    /**
     *
     * @param <T>
     * @param request
     */
    @Override
    public <T extends LwM2mResponse> void interceptUplinkRequest(UplinkRequest<T> request)
    {
        StringBuilder bldrStr = new StringBuilder();
        if (request instanceof BootstrapRequest) {
            BootstrapRequest bstrapReq = (BootstrapRequest) request;
            bldrStr.append("Bootstrap Request: Endpoint ")
                    .append(bstrapReq.getEndpointName())
                    .append(" [")
                    .append(DmCommons.getOneM2mTimeStamp())
                    .append("]");
            displayInterceptedMsg(bldrStr.toString());
        } else if (request instanceof UpdateRequest) {
            UpdateRequest updateRequest = (UpdateRequest) request;
            bldrStr.append("Update Request: Registration ID ")
                    .append(updateRequest.getRegistrationId())
                    .append(" [")
                    .append(DmCommons.getOneM2mTimeStamp())
                    .append("]");
            displayInterceptedMsg(bldrStr.toString());
        } else if (request instanceof RegisterRequest) {
            RegisterRequest registerRequest = (RegisterRequest) request;
            bldrStr.append("Register Request: Endpoint ")
                    .append(registerRequest.getEndpointName())
                    .append(" [")
                    .append(DmCommons.getOneM2mTimeStamp())
                    .append("]");
            displayInterceptedMsg(bldrStr.toString());
        } else if (request instanceof DeregisterRequest) {
            DeregisterRequest deregisterRequest = (DeregisterRequest) request;
            bldrStr.append("Deregister Request: Registration ID ")
                    .append(deregisterRequest.getRegistrationID())
                    .append(" [")
                    .append(DmCommons.getOneM2mTimeStamp())
                    .append("]");
            displayInterceptedMsg(bldrStr.toString());
        }
    }
    
    public void displayInterceptedMsg(String msgDtls)
    {
//        String text = LogWindow.txtPane_SentMsgsTab.getText();
//        final StringBuilder bldStr = new StringBuilder();
//        if(text.isEmpty())
//        {
//            bldStr.append(msgDtls)
//                    .append("\n");
//
//            SwingUtilities.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
//                    LogWindow.txtPane_SentMsgsTab.setText(bldStr.toString());
//                }
//            });
//        }else
//        {
//            bldStr.append(text)
//                     .append(msgDtls)
//                    .append("\n");                
//            SwingUtilities.invokeLater(new Runnable() 
//            {    
//                @Override           
//                public void run() {
//                    LogWindow.txtPane_SentMsgsTab.setText(bldStr.toString());
//                }
//            });
//        }
    }
    
}
