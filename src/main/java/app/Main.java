package app;

import app.config.*;
import app.controllers.*;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Mappers
        UserMapper.setConnectionPool(connectionPool);
        CustomerMapper.setConnectionPool(connectionPool);
        OrderMapper.setConnectionPool(connectionPool);
        CustomerMapper.setConnectionPool(connectionPool);
        MaterialMapper.setConnectionPool(connectionPool);
        PartslistMapper.setConnectionPool(connectionPool);

        // Routing
        CarportController.routes(app);
        ErrorController.routes(app);
        LoginController.routes(app);
        CustomerController.routes(app);
        OrderController.routes(app);
    }
}