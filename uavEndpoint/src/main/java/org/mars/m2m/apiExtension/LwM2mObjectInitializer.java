/*******************************************************************************
 * Copyright (c) 2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
//from leshan API package org.eclipse.leshan.client.resource M5 update

package org.mars.m2m.apiExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.leshan.client.resource.LwM2mInstanceEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.client.resource.SimpleInstanceEnabler;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.util.Validate;

/**
 *
 * @modified by AG BRIGHTER
 * @see ObjectsInitializer
 */
public class LwM2mObjectInitializer extends ObjectsInitializer {    
    
    protected Map<Integer, LwM2mInstanceEnabler> instances = new HashMap<>();
    
    public LwM2mObjectInitializer() {
        super(null);
    }

    public LwM2mObjectInitializer(LwM2mModel model) {
        super(model);
    }
    
    /**
     *
     * @param objectId
     * @param clazz
     */
    @Override
    public void setClassForObject(int objectId, Class<? extends LwM2mInstanceEnabler> clazz) {
        if (model.getObjectModel(objectId) == null) {
            throw new IllegalStateException("Cannot set Instance Class for Object " + objectId
                    + " because no model is defined for this id.");
        }

        Validate.notNull(clazz);
        if (instances.containsKey(objectId)) {
            throw new IllegalStateException("Cannot set Instance Class for Object " + objectId
                    + " when Instance already exists. Can only have one or the other.");
        }

        // check clazz has a default constructor
        try {
            clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Class must have a default constructor");
        }
        classes.put(objectId, clazz);
    }

    /**
     *
     * @param objectId
     * @param instance
     */
    public void setInstanceForObject(int objectId, LwM2mInstanceEnabler instance) {
        if (model.getObjectModel(objectId) == null) {
            throw new IllegalStateException("Cannot set Instance Class for Object " + objectId
                    + " because no model is defined for this id.");
        }
        Validate.notNull(instance);
        if (classes.containsKey(objectId)) {
            throw new IllegalStateException("Cannot set Instance for Object " + objectId
                    + " when Instance Class already exists.  Can only have one or the other.");
        }

        // check class of the instance has a default constructor
        try {
            instance.getClass().getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Instance must have a class with a default constructor");
        }
        instances.put(objectId, instance);
    }

    /**
     *
     * @return
     */
    @Override
    public List<ObjectEnabler> createMandatory() {
        Collection<ObjectModel> objectModels = model.getObjectModels();

        List<ObjectEnabler> enablers = new ArrayList<>();
        for (ObjectModel objectModel : objectModels) {
            if (objectModel.mandatory) {
                ObjectEnabler objectEnabler = createNodeEnabler(objectModel);
                if (objectEnabler != null)
                    enablers.add(objectEnabler);
            }
        }
        return enablers;
    }

    /**
     *
     * @param objectId
     * @return
     */
    @Override
    public List<ObjectEnabler> create(int... objectId) {
        List<ObjectEnabler> enablers = new ArrayList<>();
        for (int i = 0; i < objectId.length; i++) {
            ObjectModel objectModel = model.getObjectModel(objectId[i]);
            if (objectModel == null) {
                throw new IllegalStateException("Cannot create object for id " + objectId[i]
                        + " because no model is defined for this id.");
            }

            ObjectEnabler objectEnabler = createNodeEnabler(objectModel);
            if (objectEnabler != null)
                enablers.add(objectEnabler);
        }
        return enablers;
    }

    /**
     *
     * @param objectModel
     * @return
     */
    protected Class<? extends LwM2mInstanceEnabler> getClassFor(ObjectModel objectModel) {
        // if we have a class for this object id, return it
        Class<? extends LwM2mInstanceEnabler> clazz = classes.get(objectModel.id);
        if (clazz != null)
            return clazz;

        // if there are no class for this object check in instance list.
        LwM2mInstanceEnabler instance = instances.get(objectModel.id);
        if (instance != null)
            return instance.getClass();

        // default class :
        return SimpleInstanceEnabler.class;
    }

    /**
     *
     * @param objectModel
     * @return
     */
    @Override
    protected ObjectEnabler createNodeEnabler(ObjectModel objectModel) {
        final Map<Integer, LwM2mInstanceEnabler> lwm2m_instances;
        lwm2m_instances = new HashMap<>();
        if (!objectModel.multiple) {
            LwM2mInstanceEnabler newInstance = createInstance(objectModel);
            if (newInstance != null) {
                lwm2m_instances.put(0, newInstance);
                return new ObjectEnabler(objectModel.id, objectModel, lwm2m_instances, getClassFor(objectModel));
            }
        }
        return new ObjectEnabler(objectModel.id, objectModel, lwm2m_instances, getClassFor(objectModel));
    }

    /**
     *
     * @param objectModel
     * @return
     */
    //@Override
    protected LwM2mInstanceEnabler createInstance(ObjectModel objectModel) {
        LwM2mInstanceEnabler instance;
        if (instances.containsKey(objectModel.id)) {
            instance = instances.get(objectModel.id);
        } else {
            Class<? extends LwM2mInstanceEnabler> clazz = getClassFor(objectModel);
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        instance.setObjectModel(objectModel);
        return instance;
    }
}
