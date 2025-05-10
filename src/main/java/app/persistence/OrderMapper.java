package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool connectionPool) {
        OrderMapper.connectionPool = connectionPool;
    }

    public static Order toOrderAndSave(Order o) throws DatabaseException {

        final String sql = """
                    INSERT INTO orders (
                      customer_id,
                      width,
                      length,
                      total,
                      status,
                      comments,
                      created_at
                    ) VALUES (?,?,?,?,?,?,?)
                    RETURNING id
                """;

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, o.getCustomerId());
            ps.setInt(2, o.getWidthCm());
            ps.setInt(3, o.getLengthCm());
            ps.setDouble(4, o.getTotal());
            ps.setString(5, o.getStatus());
            ps.setString(6, o.getComments());
            ps.setTimestamp(7, Timestamp.valueOf(o.getCreatedAt()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o.setId(rs.getInt("id"));
                } else {
                    throw new DatabaseException("Could not register order");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return o;
    }

    public static Order getOrderByid(int id) throws DatabaseException{
        String sql = "SELECT * FROM Order JOIN customers c ON o.customer_id=c.id WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Date createdAt = rs.getDate("created_at");
                Double total = rs.getDouble("total");
                String status = rs.getString("status");
                int customerId = rs.getInt("customer_Id");
                int partslistId = rs.getInt("partslist_Id");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                String comment = rs.getString("comments");

                String fullname = rs.getString("fullname");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phone_number");
                int assignedSalesRepId = rs.getInt("user_id");
                int postalCode = rs.getInt("postal_code");

                Customers customer = new Customers(customerId, fullname, email, address, phoneNumber, assignedSalesRepId, postalCode);

                return new Order(id
                        , createdAt
                        , total
                        , status
                        , customer
                        , partslistId
                        , length
                        , width
                        , comment
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return null;
    }

    public static List<Order> getListofOrders() throws DatabaseException{
        String sql = "SELECT * FROM orders o JOIN customers c ON o.customer_id=c.id";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ArrayList<Order> orders = new ArrayList();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //todo: get customer object
                int id = rs.getInt("id");
                Date createdAt = rs.getDate("created_at");
                Double total = rs.getDouble("total");
                String status = rs.getString("status");
                int customerId = rs.getInt("customer_Id");
                int partslistId = rs.getInt("partslist_Id");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                String comment = rs.getString("comments");

                String fullname = rs.getString("fullname");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phone_number");
                int assignedSalesRepId = rs.getInt("user_id");
                int postalCode = rs.getInt("postal_code");

                Customers customer = new Customers(customerId, fullname, email, address, phoneNumber, assignedSalesRepId, postalCode);

                orders.add(new Order(id
                        , createdAt
                        , total
                        , status
                        , customer
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

    public static int updateTotalByOrderId(int orderId, double total) throws DatabaseException {
        String sql = "UPDATE orders SET total = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, total);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DatabaseException("Could not update order");
            }
            else return rowsAffected;

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    //requires a object with an id
    public static void UpdateOrderByObject(Order order) throws DatabaseException
    {
        String sql = "UPDATE orders SET created_at = ?, total = ?, status = ?, customer_id = ?, partslist_id = ?, length = ?, width = ?, comment = ? WHERE id = ?;";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                //updated variables
                ps.setDate(1,order.getCreatedAt());
                ps.setDouble(2,order.getTotal());
                ps.setString(3,order.getStatus());
                ps.setInt(4,order.getCustomer().getId());
                ps.setInt(5,order.getPartslist_id());
                ps.setInt(6,order.getLength());
                ps.setInt(7,order.getWidth());
                ps.setString(8,order.getComment());
                //where
                ps.setInt(9,order.getId());

                ps.executeQuery();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
}