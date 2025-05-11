package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import app.util.CheckUserUtil;
import app.util.PasswordUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class OrderController {
    private static ConnectionPool connectionPool;

    public OrderController(ConnectionPool pool) {
        connectionPool = pool;
    }

    public static void routes(Javalin app) {
        app.get("/orders", OrderController::showOrdersPage);
        app.get("/orders/manageOrder{id}", OrderController::manageOrderPage);
        app.post("/orders/manageOrder{id}", OrderController::updateOrderPage);
    }


    public static void showOrdersPage(Context ctx) {
        if(CheckUserUtil.usersOnlyCheck(ctx)) {
            try {
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
        if(CheckUserUtil.usersOnlyCheck(ctx)) {
            int orderId = Integer.parseInt(ctx.pathParam("id"));
            try {
                Order order = OrderMapper.getOrderByid(orderId);
                ctx.attribute("order", order);
                ctx.attribute("customer", CustomerMapper.getCustomerWithId(order.getCustomerId()));
            } catch (DatabaseException e) {
                ctx.attribute("error", "fejl ved at hente fra db: " + e.toString());
            }
        }
        ctx.render("orders-manage.html");
    }
    public static void updateOrderPage(Context ctx) {
        //todo:do
    }
}