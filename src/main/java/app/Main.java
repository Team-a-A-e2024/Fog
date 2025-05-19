package app;

import app.Enums.Role;
import app.config.*;
import app.controllers.*;
import app.entities.User;
import app.persistence.*;
import app.service.CarportSvgGenerator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final String URL = System.getenv("DB_URL") + "public";
    private static final String DB = System.getenv("DB");

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
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
        app.beforeMatched(ctx -> {
            String path = ctx.path();
            if (!(path.endsWith(".css") || path.endsWith(".js") || path.startsWith("/public/"))) {
                Role userRole = Role.ANYONE;
                User user = ctx.sessionAttribute("user");
                if (user != null){
                    try {
                        userRole = Role.valueOf(user.getRole().toUpperCase());
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
                //if the route doesn't allow anyone and the user doesn't have the role required
                if (!(ctx.routeRoles().contains(Role.ANYONE) || ctx.routeRoles().contains(userRole))) {
                    throw new ForbiddenResponse();
                }
            }
        });

        ServiceController.routes(app);
        CarportController.routes(app);
        ErrorController.routes(app);
        LoginController.routes(app);
        CustomerController.routes(app);
        OrderController.routes(app);

    }

    public static void svgtest(Context ctx) {

        CarportSvgGenerator svg = new CarportSvgGenerator(0, 0, 240, 660);

        ctx.attribute("svg", svg.getSvg().toString());
        ctx.render("svgPlayground");
    }
}