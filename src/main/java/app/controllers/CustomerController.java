package app.controllers;

import app.Enums.Role;
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

    public static void routes(Javalin app) {
        app.get("/customer-overview", CustomerController::customerOverview, Role.SALESREP, Role.ADMIN);
        app.post("/customer-overview", CustomerController::assignSalesRep, Role.SALESREP, Role.ADMIN);
    }

    public static void customerOverview(Context ctx) {
        try {
            User user = ctx.sessionAttribute("user");
            ctx.attribute("user", user);
            List<Customer> customerList = CustomerMapper.getCustomersWithoutSalesRep(user.getId());
            ctx.attribute("customers", customerList);
            ctx.render("customer-overview.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt, prøv igen senere. " + e.getMessage());
            ctx.render("customer-overview.html");
        }
    }

    public static void assignSalesRep(Context ctx) {
        try {
            int customerId = Integer.parseInt(ctx.formParam("customerId"));
            int salesRepId = Integer.parseInt(ctx.formParam("salesRepId"));
            CustomerMapper.assignSalesRepToCostumer(customerId, salesRepId);
            ctx.redirect("/customer-overview");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt, prøv igen senere. " + e.getMessage());
            ctx.render("customer-overview.html");
        }
    }
}