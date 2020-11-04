package com.sacchon.restapi;

import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.router.SacchonRouter;
import com.sacchon.restapi.security.SacchonRole;
import com.sacchon.restapi.security.Shield;
import com.sacchon.restapi.security.cors.CustomCorsFilter;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Role;

import javax.persistence.EntityManager;
import java.util.logging.Logger;


public class MainApplication extends Application {

    public static final Logger LOGGER = Engine.getLogger(MainApplication.class);

    public static void main(String[] args) throws Exception{
        LOGGER.info("Application starting...");

        EntityManager em = JpaUtil.getEntityManager();
        em.close();

        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9002);
        c.getDefaultHost().attach("/v1", new MainApplication());
        c.start();

        LOGGER.info("API started");
    }

    public MainApplication() {
        getRoles().add(new Role(this, SacchonRole.ROLE_PATIENT.getRoleName()));
        getRoles().add(new Role(this, SacchonRole.ROLE_DOCTOR.getRoleName()));
        getRoles().add(new Role(this, SacchonRole.ROLE_ADMIN.getRoleName()));
    }

    @Override
    public Restlet createInboundRoot() {
        
        SacchonRouter sacchonRouter = new SacchonRouter(this);
        Shield shield = new Shield(this);

        Router publicRouter = sacchonRouter.publicResources();
        ChallengeAuthenticator apiGuard = shield.createApiGuard();


        Router apiRouter = sacchonRouter.createRouter();
        apiGuard.setNext(apiRouter);

        publicRouter.attachDefault(apiGuard);

        CustomCorsFilter corsFilter = new CustomCorsFilter(this);
        return corsFilter.createCorsFilter(publicRouter);

    }
}
