package zju.homework.Controller;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Component;
import zju.homework.Model.Annotation;
import zju.homework.Model.ModelHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stardust on 2016/12/1.
 */

@Component
@Path("/annotation")
public class AnnotationController {


    ModelHandler handler = new ModelHandler();

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadAnnotation(Annotation annotation){
        handler.insert(annotation);
        return Response.ok().entity("Upload Annotation Success").build();
    }

    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Annotation> downloadAnnotatoin(@PathParam("groupId") String groupId){
        MongoCursor<Document> cursor = handler.findAnnotations(groupId).iterator();
        List<Annotation> result = new ArrayList<>();
        try {

            while ( cursor.hasNext() ){
                Document doc = cursor.next();
                result.add(new Annotation(doc.getString("author"),
                        doc.getString("data"),
                        doc.getString("groupId")));
            }
        }finally {
            cursor.close();
        }
        return result;
    }

}
