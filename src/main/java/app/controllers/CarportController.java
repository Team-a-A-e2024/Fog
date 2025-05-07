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

    public CarportController(Javalin app) {
        app.get("/carport", this::showForm);
        app.post("/carport", this::submitForm);
        app.get("/confirmation", this::showConfirmation);
    }

    private void showForm(Context ctx) {
        ctx.render("carport-form.html", Map.of("customer", new Customer(), "order", new Order(), "errors", new HashMap<String, String>()));
    }

    private void submitForm(Context ctx) {
        Map<String, String> errors = new HashMap<>();


        Customer customer = new Customer(0, ctx.formParam("fullName"), ctx.formParam("address"), parseInt(ctx.formParam("postalCode")), ctx.formParam("email"), ctx.formParam("phoneNumber"), 0);

        try {
            customer = CustomerMapper.save(customer);
        } catch (DatabaseException e) {
            errors.put("database", "Kunne ikke oprette kunde: " + e.getMessage());
            ctx.render("carport-form.html", Map.of("customer", new Customer(), "order", new Order(), "errors", errors));
            return;
        }

        Order order = new Order();                 // tom
        order.setCustomerId(customer.getId());
        order.setWidthCm(parseInt(ctx.formParam("widthCm")));
        order.setLengthCm(parseInt(ctx.formParam("lengthCm")));
        order.setTotal(0.0);
        order.setStatus("new");
        order.setComments(ctx.formParam("comments"));
        order.setCreatedAt(LocalDateTime.now());

        ctx.redirect("/confirmation");
    }


    private void showConfirmation(Context ctx) {
        ctx.render("carport-confirm.html");
    }

    private static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
