package zju.homework.Controller;

import org.bson.Document;
import org.springframework.stereotype.Component;
import zju.homework.Model.Account;
import zju.homework.Model.ModelHandler;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by stardust on 2016/11/29.
 */

@Component
@Path("/account")
public class AccountController {

    private ModelHandler accountTable = new ModelHandler();

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Account account(){
//        Account account = new Account("email", "passwd", null);
//        accountTable.insert(account);
//        return account;
//    }

    @GET
//    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(){
        return Response.ok().entity("Please Login First").build();
    }

    @POST
//    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(Account account){
        Document document = accountTable.findAccount(account.getEmail());
        if( document == null ){
            return Response.ok().entity("Account Not Found").build();
        }else if( document.getString("passwd").equals(account.getPasswd()) ){
            return Response.ok().entity("Login Success").build();
        }
        return Response.ok().entity("Incorrect Password").build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(Account account){
        Document document = accountTable.findAccount(account.getEmail());
        if( document != null ){
            return Response.ok().entity("Account Already Exist").build();
        }else {
            accountTable.insert(account);
            return Response.ok().entity("Register Success").build();
        }
    }



}
