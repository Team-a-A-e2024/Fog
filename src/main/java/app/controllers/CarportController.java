package app.controllers;

import app.Enums.Role;
import app.entities.Customer;
import app.entities.Order;
import app.entities.Partslist;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.persistence.PartslistMapper;
import app.service.BeamCalculationStrategy;
import app.service.Calculator;
import app.service.PostCalculationStrategy;
import app.service.RaftersCalculationStrategy;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarportController {

    public static void routes(Javalin app) {
        app.get ("/",      CarportController::showForm, Role.ANYONE);
        app.post("/",      CarportController::submitForm, Role.ANYONE);
        app.get ("/confirmation", CarportController::showConfirmation, Role.ANYONE);
    }

    // GET  /carport – render an empty form
    private static void showForm(Context ctx) {
        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);
        ctx.render("carport-form.html");
    }

    // POST /carport – save customer + order, then redirect
    private static void submitForm(Context ctx) {

        Map<String,String> errors = new HashMap<>();

        // Builds CUSTOMER
        Customer customer = new Customer(
                ctx.formParam("fullName"),
                ctx.formParam("email"),
                ctx.formParam("address"),
                ctx.formParam("phoneNumber"),
                parseInt(ctx.formParam("postalCode"))

        );

        try {
            customer = CustomerMapper.save(customer);
        } catch (DatabaseException e) {
            errors.put("database", "Could not create customer: " + e.getMessage());
            ctx.render("carport-form.html", Map.of(
                    "errors",   errors));
            return;
        }

        // Build & saves ORDER
        Order order = new Order(
                customer.getId(),
                0.0,
                "Afventer",
                parseInt(ctx.formParam("widthCm")),
                parseInt(ctx.formParam("lengthCm")),
                ctx.formParam("comments"),
                LocalDateTime.now()
        );

        try {
            Order savedOrder = OrderMapper.toOrderAndSave(order);

            Calculator calculator = new Calculator(order.getLengthCm(), order.getWidthCm());
            calculator.addStrategy(new PostCalculationStrategy(), MaterialMapper.getMaterialById(1));
            calculator.addStrategy(new BeamCalculationStrategy(), MaterialMapper.getMaterialById(2));
            calculator.addStrategy(new RaftersCalculationStrategy(), MaterialMapper.getMaterialById(2));
            List<Partslist> partlist = calculator.getPartslists();
            double total = calculator.getTotalPrice();

            for (Partslist part : partlist) {
                PartslistMapper.createPartslist(part, savedOrder.getId());
            }

            OrderMapper.updateTotalByOrderId(savedOrder.getId(), total);
        } catch (DatabaseException e) {
            errors.put("database", "Could not create order: " + e.getMessage());
            ctx.render("carport-form.html", Map.of(
                    "errors",   errors));
            return;
        }

        ctx.redirect("/confirmation");
    }

    private static void showConfirmation(Context ctx) {
        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);
        ctx.render("carport-confirm.html");
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return 0; }
    }
}
