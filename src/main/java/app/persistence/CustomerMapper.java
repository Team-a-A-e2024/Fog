package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class CustomerMapper {

    public static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool pool) {
        CustomerMapper.connectionPool = pool;
    }
    public static Customer toCustomerAndSave(io.javalin.http.Context ctx) throws DatabaseException {
        Customer c = new Customer();
        c.setFullName(ctx.formParam("fullName"));
        c.setStreetAddress(ctx.formParam("streetAddress"));
        c.setPostalCode(ctx.formParam("postalCode"));
        c.setEmail(ctx.formParam("email"));
        c.setPhone(ctx.formParam("phoneNumber"));
        c.setConsent("on".equals(ctx.formParam("consent")));

        String sql = """
            INSERT INTO customer(
              full_name, street_addr, postal_code,
              email, phone, consent
            ) VALUES (?,?,?,?,?,?)
            RETURNING id
        """;

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getStreetAddress());
            ps.setString(3, c.getPostalCode());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhone());
            ps.setBoolean(6, c.isConsent());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c.setId(rs.getInt("id"));
                } else {
                    throw new DatabaseException("Kunne ikke oprette kunde");
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }

        return c;
    }
}
