package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.util.PasswordUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class LoginController {
    private static ConnectionPool connectionPool;

    public LoginController(ConnectionPool pool) {
        connectionPool = pool;
    }

    public static void routes(Javalin app) {
        app.get("/login", LoginController::showLoginPage);
        app.post("/login", LoginController::handleLogin);
        app.get("/logout", LoginController::logout);
    }

    public static void showLoginPage(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user != null) {
            ctx.attribute("email", user.getEmail());
        }
        ctx.render("login.html");
    }

    public static void handleLogin(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try{
            User user = UserMapper.getUserByEmail(email);
            if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
                ctx.sessionAttribute("user", user);
                ctx.redirect("/");
                return;
            }
        }catch (DatabaseException e){
            e.printStackTrace();
        }
        ctx.attribute("error", "Invalid email or password");
        ctx.render("login.html");
    }

    public static void logout(Context ctx) {
        //this deletes the entire session
        ctx.req().getSession().invalidate();
        ctx.redirect("/login");
    }
}
