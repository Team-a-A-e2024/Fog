package app.controllers;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ErrorController {
    private static ConnectionPool connectionPool;

    public ErrorController(ConnectionPool pool) {
        connectionPool = pool;
    }

    public static void routes(Javalin app) {
        app.get("/error/403", ErrorController::error403);
    }

    public static void error403(Context ctx) {
        ctx.attribute("errorCode", "Fejl 403:");
        ctx.attribute("errorText", "adgang forbudt");
        ctx.render("error.html");
    }
}