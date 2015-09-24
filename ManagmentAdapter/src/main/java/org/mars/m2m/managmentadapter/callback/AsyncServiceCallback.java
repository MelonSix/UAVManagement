/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.callback;

/**
 * Called to implement a custom operation when a
 * client performs an asynchronous request to consume
 * a web service/resource
 * @author AG BRIGHTER
 * @param <T> The type of the expected data to be passed to the method
 */
public interface AsyncServiceCallback<T> {

    /**
     * The method to be called to implement
     * custom operations after a response is received
     * by the client consuming a web resource 
     * @param t The data object
     */
    void asyncServicePerformed(T t);
}
