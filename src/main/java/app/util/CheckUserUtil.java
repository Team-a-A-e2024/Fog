package app.util;

import app.entities.User;
import io.javalin.http.Context;

public class CheckUserUtil {

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
