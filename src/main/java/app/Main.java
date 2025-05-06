package app;

import app.controllers.CarportController;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER     = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL      = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB       = "fog";

    private static final ConnectionPool connectionPool =
            ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        Javalin app = Javalin.create(cfg -> {
            cfg.staticFiles.add("/public", Location.CLASSPATH);      // maps src/main/resources/public → /
            cfg.jetty.modifyServletContextHandler(h ->
                    h.setSessionHandler(SessionConfig.sessionConfig()));
            cfg.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);


        CustomerMapper.setConnectionPool(connectionPool);
        OrderMapper.setConnectionPool(connectionPool);

        new CarportController(app);


        app.get("/", ctx -> ctx.redirect("/carport"));

        System.out.println("Server kører på http://localhost:7070/carport");
    }
}
