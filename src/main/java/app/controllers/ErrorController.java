package app.controllers;
import app.Enums.Role;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ErrorController {

    public static void routes(Javalin app) {
        app.get("/error/403", ErrorController::error403, Role.ANYONE);
    }

    public static void error403(Context ctx) {
        ctx.attribute("errorCode", "Fejl 403:");
        ctx.attribute("errorText", "adgang forbudt");
        ctx.render("error.html");
    }
}