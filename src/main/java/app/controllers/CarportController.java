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

    // Register routes when application starts
    public CarportController(Javalin app) {
        app.get ("/carport",      this::showForm);
        app.post("/carport",      this::submitForm);
        app.get ("/confirmation", this::showConfirmation);
    }

    // GET  /carport – render an empty form
    private void showForm(Context ctx) {
        ctx.render("carport-form.html", Map.of(
                "customer", new Customer(),          // blank DTOs for binding
                "order",    new Order(),
                "errors",   new HashMap<String,String>()
        ));
    }

    // POST /carport – save customer + order, then redirect
    private void submitForm(Context ctx) {

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
                0,                                   // id → auto
                customer.getId(),                    // FK (NOT NULL)
                0.0,                                 // total  (NOT NULL)
                "new",                               // status (NOT NULL)
                parseInt(ctx.formParam("widthCm")),  // width  (NOT NULL)
                parseInt(ctx.formParam("lengthCm")), // length (NOT NULL)
                ctx.formParam("comments"),           // nullable
                LocalDateTime.now()                  // created_at (NOT NULL)
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

    private void showConfirmation(Context ctx) {
        ctx.render("carport-confirm.html");
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return 0; }
    }
}
