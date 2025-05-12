package app.controllers;

import app.util.CheckUserUtil;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.util.PasswordUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Date;
import java.time.LocalDate;

public class LoginController {

    public static void routes(Javalin app) {
        app.get("/login", LoginController::showLoginPage);
        app.post("/login", LoginController::handleLogin);
        app.get("/logout", LoginController::logout);
        app.get("/changePassword", LoginController::showChangePasswordPage);
        app.post("/changePassword", LoginController::HandleChangePassword);
    }

    public static void showLoginPage(Context ctx) {
        if (CheckUserUtil.loginCheck(ctx)) {
            User user = ctx.sessionAttribute("user");
            ctx.attribute("email", user.getEmail());
        }
        ctx.render("login.html");
    }

    public static void handleLogin(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try{
            User user = UserMapper.getUserByEmail(email);
            if(user.getPasswordChangeDate() == null){
                if(PasswordUtil.checkPlainPassword(password,user.getPassword())){
                    ctx.sessionAttribute("user", user);
                    ctx.redirect("/changePassword");
                    return;
                }
            }
            if (user != null && PasswordUtil.checkHashedPassword(password, user.getPassword())) {
                ctx.sessionAttribute("user", user);
                ctx.redirect("/customer-overview");
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

    public static void showChangePasswordPage(Context ctx){
        if(CheckUserUtil.usersOnlyCheck(ctx)){
            User user = ctx.sessionAttribute("user");
            ctx.attribute("email", user.getEmail());
            ctx.render("change-password.html");
        }
    }

    public static void HandleChangePassword(Context ctx) {
        String password = ctx.formParam("password");
        String password2 = ctx.formParam("password-repeat");

        if(password.equals(password2)){
            if(password.length()<51 && password.length()>15){
                User user = ctx.sessionAttribute("user");
                user.setPassword(PasswordUtil.hashPassword(password));
                user.setPasswordChangeDate(Date.valueOf(LocalDate.now()));

                try{
                    UserMapper.updateUserByObject(user);
                    ctx.sessionAttribute("user");
                    ctx.redirect("/customer-overview");
                    return;
                }
                catch (DatabaseException e){
                    ctx.attribute("error", "failed to update db: " + e);
                }
            }
            else {
                ctx.attribute("error", "Password skal være mellem 16 og 50 tegn");
            }
        }else{
            ctx.attribute("error", "Password skal være ens");
        }
        ctx.render("change-password.html");
    }
}