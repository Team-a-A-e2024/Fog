package app.controllers;

import app.entities.Customers;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class CustomerController {
    private static ConnectionPool connectionPool;

    public CustomerController(ConnectionPool pool) {
        connectionPool = pool;
    }

    public static void routes(Javalin app) {
    app.get("/customer-overview", CustomerController::customerOverview);
    }

    public static void customerOverview(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.redirect("/login");
            return;
        }
        ctx.attribute("user", currentUser);
        try {
            int userId = currentUser.getId();
            List<Customers> customerList = CustomerMapper.getCustomersWithoutSalesRep(userId);
            ctx.attribute("customers", customerList);
            ctx.render("customer-overview.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt: " + e.getMessage());
            ctx.redirect("error.html");
        }
    }
}
