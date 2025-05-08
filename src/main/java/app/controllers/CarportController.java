package app.controllers;

import app.entities.Customer;
import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class CarportController {

    public static void routes(Javalin app) {
        app.get ("/",      CarportController::showForm);
        app.post("/",      CarportController::submitForm);
        app.get ("/confirmation", CarportController::showConfirmation);    }
    // Register routes when application starts
    public CarportController(Javalin app) {
    }

    // GET  /carport – render an empty form
    private static void showForm(Context ctx) {
        ctx.render("carport-form.html", Map.of(
                "customer", new Customer(),
                "order",    new Order(),
                "errors",   new HashMap<String,String>()
        ));
    }

    // POST /carport – save customer + order, then redirect
    private static void submitForm(Context ctx) {

        Map<String,String> errors = new HashMap<>();

        // Build & saves CUSTOMER
        Customer customer = new Customer(
                0,                                   // id → auto-generated
                ctx.formParam("fullname"),
                ctx.formParam("address"),
                parseInt(ctx.formParam("postalCode")),
                ctx.formParam("email"),
                ctx.formParam("phoneNumber"),
                0                                    // userId (optional)
        );

        try {
            customer = CustomerMapper.save(customer);
        } catch (DatabaseException e) {
            errors.put("database", "Could not create customer: " + e.getMessage());
            ctx.render("carport-form.html", Map.of(
                    "customer", new Customer(),
                    "order",    new Order(),
                    "errors",   errors));
            return;
        }

        // Build & saves ORDER
        Order order = new Order(
                0,
                customer.getId(),
                0.0,
                "new",
                parseInt(ctx.formParam("widthCm")),
                parseInt(ctx.formParam("lengthCm")),
                ctx.formParam("comments"),
                LocalDateTime.now()
        );

        try {
            OrderMapper.toOrderAndSave(order);
        } catch (DatabaseException e) {
            errors.put("database", "Could not create order: " + e.getMessage());
            ctx.render("carport-form.html", Map.of(
                    "customer", customer,
                    "order",    new Order(),
                    "errors",   errors));
            return;
        }

        ctx.redirect("/confirmation");
    }

    private static void showConfirmation(Context ctx) {
        ctx.render("carport-confirm.html");
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return 0; }
    }
}
