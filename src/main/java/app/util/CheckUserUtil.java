package app.util;

import app.Enums.Role;
import app.controllers.ErrorController;
import app.entities.User;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;

public class CheckUserUtil {

    public static Role getUserRole(Context ctx){
        User user = ctx.sessionAttribute("user");
        if (user != null){
            try {
                return Role.valueOf(user.getRole().toUpperCase());
            }catch (IllegalArgumentException e){
                ErrorController.error403(ctx);
            }
        }
        return Role.ANYONE;
    }
}