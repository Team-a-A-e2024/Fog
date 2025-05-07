package app;

import app.controllers.CarportController;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.ErrorController;
import app.controllers.CustomerController;
import app.controllers.LoginController;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.UserMapper;
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
        CustomerMapper.SetConnectionPool(connectionPool);

        CustomerMapper.setConnectionPool(connectionPool);
        OrderMapper.setConnectionPool(connectionPool);

        new CarportController(app);


        // Routing
        ErrorController.routes(app);
        LoginController.routes(app);
        CustomerController.routes(app);
        app.get("/", ctx ->  ctx.render("index.html"));
    }
}