package services;

import brugerautorisation.jwt.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
@ApplicationPath("/rest")
public class MyApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public MyApplication () {
        register(AuthenticationFilter.class);
        register(UserService.class);
        register(GameService.class);
        register(HighscoreService.class);
    }

//    @Override
//    public Set<Class<?>> getClasses() {
//        HashSet h = new HashSet<Class<?>>();
//        h.add(AuthenticationFilter.class);
//        h.add(UserService.class);
//        h.add(GameService.class);
//        return h;
//    }
}