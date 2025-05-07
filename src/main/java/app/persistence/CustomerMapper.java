package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;

import java.sql.*;

public class CustomerMapper {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool pool) {
        connectionPool = pool;
    }


    public static Customer save(Customer c) throws DatabaseException {

        final String sql = """
                    INSERT INTO customers (
                      fullname,
                      address,     
                      postal_code,
                      email,
                      phone_number,
                      user_id
                    ) VALUES (?,?,?,?,?,?)
                    RETURNING id
                """;

        try (Connection con = connectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getAddress());
            ps.setInt(3, c.getPostalCode());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhoneNumber());
            if (c.getUserId() == 0) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, c.getUserId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) c.setId(rs.getInt("id"));
                else throw new DatabaseException("Could not register customer");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return c;
    }
}