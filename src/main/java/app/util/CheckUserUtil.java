package app.util;

import app.Enums.Role;
import app.entities.User;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;

public class CheckUserUtil {

    public static Role getUserRole(Context ctx){
        User user = ctx.sessionAttribute("user");
        if (user != null){
            try {
                return Role.valueOf(user.getRole().toUpperCase());
            }catch (IllegalArgumentException e){}
        }
        return Role.ANYONE;
    }

    public static boolean usersOnlyCheck(Context ctx){
        if(!loginCheck(ctx)){
            ctx.redirect("/error/403");
            return false;
        }
        return true;
    }

    public static boolean loginCheck(Context ctx){
        User user = ctx.sessionAttribute("user");
        return (user != null);
    }
}
