package app.controllers;

import app.entities.Customer;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.util.CheckUserUtil;
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
        CheckUserUtil.usersOnlyCheck(ctx);
        try {
            User user = ctx.sessionAttribute("user");
            ctx.attribute("email", user.getEmail());
            int userId = user.getId();
            List<Customer> customerList = CustomerMapper.getCustomersWithoutSalesRep(userId);
            ctx.attribute("customers", customerList);
            ctx.render("customer-overview.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt, prøv igen senere. " + e.getMessage());
            ctx.render("customer-overview.html");
        }
    }
}
