package app.controllers;

import app.Enums.Role;
import app.entities.Customer;
import app.entities.Order;
import app.entities.User;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.util.EmailUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ServiceController {

    public static void routes(Javalin app) {
        app.get("/service", ServiceController::showPaymentForm, Role.ANYONE);
        app.post("/payment-confirm", ServiceController::handleSendEmail, Role.ANYONE);
    }

    // Vis betalingsformularen
    private static void showPaymentForm(Context ctx) {
        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);

        // Få orderId fra query string
        String orderIdParam = ctx.queryParam("orderId");

        if (orderIdParam == null) {
            ctx.attribute("error", "Mangler ordre-ID.");
            ctx.redirect("/orders"); // fallback
            return;
        }

        int orderId = Integer.parseInt(orderIdParam);
        ctx.attribute("orderId", orderId); // send til skabelonen
        ctx.render("payment-service.html");
    }

    // Håndter betaling + send e-mail
    private static void handleSendEmail(Context ctx) {
        try {
            int orderId = Integer.parseInt(ctx.formParam("orderId"));

            Order order = OrderMapper.getOrderByid(orderId);
            Customer customer = CustomerMapper.getCustomerWithId(order.getCustomerId());

            EmailUtil.sendOrderConfirmation(customer);
            OrderMapper.updateStatusByOrderId(orderId, "Godkendt");

            ctx.attribute("orderId", orderId);
            ctx.render("payment-confirm.html");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.attribute("emailError", "Kunne ikke sende bekræftelse: " + e.getMessage());
            ctx.redirect("/orders");
            //Todo: Når en ordre er gennemført og betalt, skal man sendes videre til payment-confirm. Men man bliver sendt videre til denne error.
            //Todo: Når der sker en error, skal kunden ikke sendes videre til orders, de skal sendes tilbage til betalingssiden med en fejlbesked.
        }
    }
}