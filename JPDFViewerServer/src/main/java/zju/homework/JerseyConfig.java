package zju.homework;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import zju.homework.Controller.AccountController;
import zju.homework.Controller.AnnotationController;
import zju.homework.Controller.GroupController;

/**
 * Created by stardust on 2016/11/29.
 */

@Configuration
public class JerseyConfig extends ResourceConfig{

    public JerseyConfig(){
        register(AccountController.class);
        register(GroupController.class);
        register(AnnotationController.class);
    }

}
