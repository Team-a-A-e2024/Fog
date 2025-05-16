package app.Enums;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ANYONE, SALESREP, ADMIN
}
