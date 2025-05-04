package app.persistence;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool connectionPool) {
        OrderMapper.connectionPool = connectionPool;
    }

    public static Order getOrderByid(int id) throws DatabaseException{
        String sql = "SELECT * FROM Order WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // todo do
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return null;
    }

    public static List<Order> getListofOrders() throws DatabaseException{
        String sql = "SELECT * FROM Order WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ArrayList<Order> orders = new ArrayList();


            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String createdAt = rs.getString("created_at");
                String total = rs.getString("total");
                String status = rs.getString("status");
                int customerId = rs.getInt("customerId");
                int partslistId = rs.getInt("partslistId");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                String comment = rs.getString("comment");

                orders.add(new Order(id
                        , createdAt
                        , total
                        , status
                        , customerId
                        , partslistId
                        , length
                        , width
                        , comment
                        ));
            }
            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
    }

    public static int UpdateOrderByObject(Order order) throws DatabaseException
    {
        String sql = "";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1,order.getId());

                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                    else {
                        throw new DatabaseException("Failed to insert user");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }




}