package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;
import java.sql.*;

public class OrderMapper {

    public static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool pool) {
        OrderMapper.connectionPool = pool;
    }

    public static Order toOrderAndSave(Context ctx, int customerId) throws DatabaseException {
        Order o = new Order();
        o.setCustomerId(customerId);
        o.setWidthCm(parseInt(ctx.formParam("widthCm")));
        o.setLengthCm(parseInt(ctx.formParam("lengthCm")));
        o.setRemarks(ctx.formParam("remarks"));


        String sql = """
            INSERT INTO carport_order(
              customer_id, width_cm, length_cm, remarks
            ) VALUES (?,?,?,?)
            RETURNING id
        """;

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, o.getCustomerId());
            ps.setInt(2, o.getWidthCm());
            ps.setInt(3, o.getLengthCm());
            ps.setString(4, o.getRemarks());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o.setId(rs.getInt("id"));
                } else {
                    throw new DatabaseException("Kunne ikke oprette ordre");
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }

        return o;
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }
}
