package app.controllers;

import app.entities.Customer;
import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.LinkedHashMap;
import java.util.Map;

public class CarportController {

    public CarportController(Javalin app) {
        app.get( "/carport",      this::showForm);
        app.post("/carport",      this::submitForm);
        app.get( "/confirmation", this::showConfirmation);
    }

    private void showForm(Context ctx) {
        ctx.render("carport-form.html", Map.of(
                "customer", new Customer(),
                "order",    new Order(),
                "errors",   new LinkedHashMap<String,String>()
        ));
    }

    private void submitForm(Context ctx) {
        var errors = new LinkedHashMap<String,String>();

        Customer customer;
        try {
            customer = CustomerMapper.toCustomerAndSave(ctx);
        } catch (DatabaseException e) {
            errors.put("database", "Kunne ikke oprette kunde: " + e.getMessage());
            ctx.render("carport-form.html", Map.of(
                    "customer", new Customer(),
                    "order",    new Order(),
                    "errors",   errors
            ));
            return;
        }

        try {
            OrderMapper.toOrderAndSave(ctx, customer.getId());
        } catch (DatabaseException e) {
            errors.put("database", "Kunne ikke oprette ordre: " + e.getMessage());
            ctx.render("carport-form.html", Map.of(
                    "customer", customer,
                    "order",    new Order(),
                    "errors",   errors
            ));
            return;
        }


        ctx.redirect("/confirmation");
    }

    private void showConfirmation(Context ctx) {
        ctx.render("carport-confirm.html");
    }
}
