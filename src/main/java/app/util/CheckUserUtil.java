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
                System.out.println(user.getRole());
                return Role.valueOf(user.getRole().toUpperCase());
            }catch (IllegalArgumentException e){}
        }
        return Role.ANYONE;
    }
}
