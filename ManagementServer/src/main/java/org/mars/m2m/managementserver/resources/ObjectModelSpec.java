package org.mars.m2m.managementserver.resources;

import com.google.gson.Gson;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.mars.m2m.dmcore.json.ConfigGson;
import org.mars.m2m.managementserver.core.CustomObjectModel;

/**
 * 
 */
@Path("/objectspecs")
public class ObjectModelSpec {
    
    private final Gson gson;

    private final CustomObjectModel modelProvider;

    public ObjectModelSpec() {
        this.gson = ConfigGson.getCustomGsonConfig();

        modelProvider = new CustomObjectModel();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectModel() {        
        LwM2mModel model = modelProvider.getObjectModel(null);
        return Response.ok(model.getObjectModels()).build();
    }
}
