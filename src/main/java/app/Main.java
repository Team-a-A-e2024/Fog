package app;

import app.config.*;
import app.controllers.*;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final String URL = System.getenv("DB_URL");
    private static final String DB = System.getenv("DB");

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