package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
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
        app.get("/orders/manageOrder/{id}", OrderController::manageOrderPage);
        app.post("/orders/manageOrder/{id}", OrderController::updateOrderPage);
    }


    public static void showOrdersPage(Context ctx) {
        CheckUserUtil.usersOnlyCheck(ctx);
        try{
            ctx.attribute("orderlist", OrderMapper.getListofOrders());
        }catch (DatabaseException e){
            ctx.attribute("error", e.toString());
        }
        ctx.render("orders.html");
    }

    public static void manageOrderPage(Context ctx) {
        CheckUserUtil.usersOnlyCheck(ctx);
        int orderId = Integer.parseInt(ctx.pathParam("id"));
        try{
            ctx.attribute("order", OrderMapper.getOrderByid(orderId));
        }catch (DatabaseException e){
            ctx.attribute("error", e.toString());
        }
    }
    public static void updateOrderPage(Context ctx) {
        CheckUserUtil.usersOnlyCheck(ctx);
        int orderId = Integer.parseInt(ctx.pathParam("id"));
        try{
            ctx.attribute("order", OrderMapper.getOrderByid(orderId));
        }catch (DatabaseException e){
            ctx.attribute("error", e.toString());
        }
    }
}