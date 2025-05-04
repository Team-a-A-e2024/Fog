package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
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
    }

    public static void showOrdersPage(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user == null) {
            ctx.redirect("/login");
        }
        //todo: call db to get a list of all orders newest to older
        ctx.attribute("orderlist", OrderMapper.getListofOrders());
        ctx.render("orders.html");
    }
}