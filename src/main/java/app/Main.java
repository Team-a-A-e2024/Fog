package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.CustomerController;
import app.controllers.LoginController;
import app.entities.Customers;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.List;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) throws DatabaseException {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Mappers
        UserMapper.setConnectionPool(connectionPool);
        CustomerMapper.SetConnectionPool(connectionPool);

        // Routing
        LoginController.routes(app);
        app.get("/", ctx ->  ctx.render("index.html"));
        CustomerController.routes(app);
        app.get("/customer-overview", ctx -> ctx.render("customer-overview.html"));
    }
}