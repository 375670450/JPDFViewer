package zju.homework.Controller;

import org.bson.Document;
import org.springframework.stereotype.Component;
import zju.homework.Model.Group;
import zju.homework.Model.ModelHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by stardust on 2016/11/29.
 */

@Component
@Path("/group")
public class GroupController {

    ModelHandler handler = new ModelHandler();

//    public GroupController(){
//
//    }

    @GET
    @Path("/{groupId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response joinGroup(@PathParam("groupId") String groupId){
        Document result = handler.findGroup(groupId);
        if( result == null ){
            return Response.ok().entity("Group Not Found").build();
        }else {
            return Response.ok().entity(result.getString("pdfData")).build();
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createGroup(Group group){
        Document result = handler.findGroup(group.getGroupId());
        if( result != null ){
            return Response.ok().entity("Group ID Existed").build();
        }else{
            handler.insert(group);
            return Response.ok().entity("Create Group Success").build();
        }
    }

}
