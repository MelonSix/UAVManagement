/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Configuration;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author AG BRIGHTER
 */

/**
 * 
 * This class provides the private and public keys for security
 */
public class UavKeyManagment {
    
    // Get public and private server key
    private PrivateKey privateKey = null;
    private PublicKey publicKey = null;
    private UAVConfiguration uavConfig = null;
        
    /**
     * Creates an object of the Key management class
     * @param uavConfig the configuration used by the uav
     */
    public UavKeyManagment(UAVConfiguration uavConfig) 
    {
        this.uavConfig = uavConfig;
        setKeys();
    }
    
    private void setKeys()
    {
        try
        {
            byte[] publicX = DatatypeConverter.parseHexBinary(uavConfig.UAV_POINT_X_HEX_STRING);
            byte[] publicY = DatatypeConverter.parseHexBinary(uavConfig.UAV_POINT_Y_HEX_STRING);
            byte[] privateS = DatatypeConverter.parseHexBinary(uavConfig.UAV_PRIVATE_KEY_HEX_STRING);

            // Get Elliptic Curve Parameter spec
            AlgorithmParameters algoParameters = AlgorithmParameters.getInstance("EC");
            algoParameters.init(new ECGenParameterSpec(uavConfig.ELLIPTIC_CURVE_SPEC));
            ECParameterSpec parameterSpec = algoParameters.getParameterSpec(ECParameterSpec.class);

            // Create key specs
            KeySpec publicKeySpec = new ECPublicKeySpec(new ECPoint(new BigInteger(publicX), new BigInteger(publicY)), parameterSpec);
            KeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(privateS), parameterSpec);
            
            // Get keys
            publicKey = KeyFactory.getInstance("EC").generatePublic(publicKeySpec);
            privateKey = KeyFactory.getInstance("EC").generatePrivate(privateKeySpec);
        }catch(InvalidKeySpecException | NoSuchAlgorithmException | InvalidParameterSpecException e)
        {
            /**
             * TODO: implement logging here
             */
            e.printStackTrace();
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    
}
