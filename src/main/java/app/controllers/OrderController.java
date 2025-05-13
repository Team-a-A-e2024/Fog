package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.util.CheckUserUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class OrderController {

    public static void routes(Javalin app) {
        app.get("/orders", OrderController::showOrdersPage);
        app.get("/orders/manageOrder{id}", OrderController::manageOrderPage);
        app.post("/orders/manageOrder{id}", OrderController::updateOrderPage);
        app.get("/orders/{id}/offer", OrderController::showOfferPage);
        app.post("/orders/{id}/offer/send", OrderController::sendOffer);
        app.post("/orders/{id}/offer/cancel", OrderController::cancelOffer);
        app.post("/orders/{id}/confirm", OrderController::confirmOrderAndSendEmail);

    }

    public static void showOrdersPage(Context ctx) {
        if (CheckUserUtil.usersOnlyCheck(ctx)) {
            try {
                User user = ctx.sessionAttribute("user");
                ctx.attribute("user", user);
                ctx.attribute("orders", OrderMapper.getListofOrders());
            } catch (DatabaseException e) {
                ctx.attribute("error", e.toString());

            }
            User user = ctx.sessionAttribute("user");
            ctx.attribute("email", user.getEmail());
            ctx.render("orders.html");
        }
    }

    public static void manageOrderPage(Context ctx) {
        if (CheckUserUtil.usersOnlyCheck(ctx)) {
            int orderId = Integer.parseInt(ctx.pathParam("id"));
            try {
                User user = ctx.sessionAttribute("user");
                ctx.attribute("user", user);
                Order order = OrderMapper.getOrderByid(orderId);

                if (order == null) {
                    ctx.attribute("servererror", "kunne ikke finde en bruger");
                    ctx.render("orders-manage.html");
                    return;
                }
                ctx.attribute("order", order);
                ctx.attribute("customer", CustomerMapper.getCustomerWithId(order.getCustomerId()));
            } catch (DatabaseException e) {
                ctx.attribute("servererror", "fejl ved at hente fra db: " + e.toString());
            }
        }
        ctx.render("orders-manage.html");
    }

    public static void updateOrderPage(Context ctx) {
        if (CheckUserUtil.usersOnlyCheck(ctx)) {
            int orderId = Integer.parseInt(ctx.pathParam("id"));
            Order order = null;
            try {
                User user = ctx.sessionAttribute("user");
                ctx.attribute("user", user);
                order = OrderMapper.getOrderByid(orderId);
                if (order == null) {
                    ctx.attribute("servererror", "kunne ikke finde en bruger");
                    ctx.render("orders-manage.html");
                    return;
                }
                ctx.attribute("order", order);
                ctx.attribute("customer", CustomerMapper.getCustomerWithId(order.getCustomerId()));
            } catch (DatabaseException e) {
                ctx.attribute("servererror", "fejl ved at hente fra db: " + e.toString());
                ctx.render("orders-manage.html");
                return;
            }

            order.setComments(ctx.formParam("comments"));
            order.setWidthCm(Integer.parseInt(ctx.formParam("widthCm")));
            order.setLengthCm(Integer.parseInt(ctx.formParam("lengthCm")));
            order.setStatus(ctx.formParam("status"));
            order.setTotal(Double.parseDouble(ctx.formParam("total")));
            try {
                OrderMapper.updateOrderByObject(order);
            } catch (DatabaseException e) {
                ctx.attribute("servererror", "fejl ved at hente fra db: " + e.toString());
                ctx.render("orders-manage.html");
                return;
            }
            ctx.attribute("servererror", "dine ændringer er gemt");
            ctx.render("orders-manage.html");
        }
    }

    private static void showOfferPage(Context ctx) {
        int orderId = Integer.parseInt(ctx.pathParam("id"));

        try {
            User user = ctx.sessionAttribute("user");
            ctx.attribute("user", user);
            Order order = OrderMapper.getOrderByid(orderId);
            ctx.attribute("order", order);
            ctx.attribute("customer", CustomerMapper.getCustomerWithId(order.getCustomerId()));
            ctx.render("offer.html");
        } catch (DatabaseException e) {
            ctx.attribute("error", "fejl ved at hente fra db: " + e.toString());
        }
    }

    private static void sendOffer(Context ctx) {
        int orderId = Integer.parseInt(ctx.pathParam("id"));

        try {
            OrderMapper.updateStatusByOrderId(orderId, "Behandles");
            manageOrderPage(ctx);
        } catch (DatabaseException e) {
            ctx.attribute("error", "fejl ved at hente fra db: " + e.toString());
        }
    }

    private static void cancelOffer(Context ctx) {
        int orderId = Integer.parseInt(ctx.pathParam("id"));

        try {
            OrderMapper.updateStatusByOrderId(orderId, "Annulleret");
            showOfferPage(ctx);
        } catch (DatabaseException e) {
            ctx.attribute("error", "fejl ved at hente fra db: " + e.toString());
        }
    }

    private static void confirmOrderAndSendEmail(Context ctx) {
        int orderId = Integer.parseInt(ctx.pathParam("id"));

        try {
            Order order = OrderMapper.getOrderByid(orderId);
            if (order == null) {
                ctx.attribute("error", "Ordre ikke fundet.");
                ctx.redirect("/orders");
                return;
            }

            var customer = CustomerMapper.getCustomerWithId(order.getCustomerId());
            if (customer == null) {
                ctx.attribute("error", "Kunde ikke fundet.");
                ctx.redirect("/orders");
                return;
            }

            EmailUtil.sendOrderConfirmation(customer);
            OrderMapper.updateStatusByOrderId(orderId, "Bekræftet");
            ctx.render("payment-confirm.html");

        } catch (Exception e) {
            ctx.attribute("error", "Fejl ved afsendelse: " + e.getMessage());
            ctx.redirect("/orders");
        }
    }

}