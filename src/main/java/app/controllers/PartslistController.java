package app.controllers;

import app.Enums.Role;
import app.entities.Order;
import app.entities.Partslist;
import app.exceptions.DatabaseException;
import app.persistence.OrderMapper;
import app.persistence.PartslistMapper;
import app.service.CarportSvgGenerator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class PartslistController {

    public static void routes(Javalin app){
        app.get("/partslist/{orderId}", PartslistController::partslistOverview, Role.ANYONE);
    }

    private static void partslistOverview(Context ctx) {
        int orderId = Integer.parseInt(ctx.pathParam("orderId"));

        try {
            List<Partslist> partslist = PartslistMapper.getPaidPartslist(orderId);
            ctx.attribute("partslist", partslist);

            Order order = OrderMapper.getOrderByid(orderId);
            CarportSvgGenerator svg = new CarportSvgGenerator(0, 0, order.getWidthCm(), order.getLengthCm());
            ctx.attribute("svg", svg.getSvg().toString());

            ctx.render("partslist.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt, prøv igen senere. " + e.getMessage());
            ctx.render("partslist.html");
            }
        }
    }

