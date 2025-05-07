package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;

public class OrderMapper {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool pool) {
        OrderMapper.connectionPool = pool;
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
}

