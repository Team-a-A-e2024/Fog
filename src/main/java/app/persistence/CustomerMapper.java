package app.persistence;

import app.entities.Customers;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {
    private static ConnectionPool connectionPool;

    public static void SetConnectionPool(ConnectionPool connectionPool) {
        CustomerMapper.connectionPool = connectionPool;
    }

    public static List<Customers> getCustomersWithoutSalesRep(int userId) throws DatabaseException {
        List<Customers> customerList = new ArrayList<>();
        String sql = "SELECT c.*, o.*, u.* " +
                "FROM customers c " +
                "JOIN orders o ON c.id = o.customer_id " +
                "LEFT JOIN users u ON c.user_id = u.id " +
                "WHERE (c.user_id IS NULL OR c.user_id = ?) " +
                "AND o.status = 'Afventer';";
        try {
            Connection connec = connectionPool.getConnection();
            PreparedStatement ps = connec.prepareStatement(sql);
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int customerId = rs.getInt("id");
                    String fullname = rs.getString("fullname");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    int phoneNumber = rs.getInt("phone_number");
                    int salesRepId = rs.getInt("user_id");
                    int postalCode = rs.getInt("postal_code");

                    Customers customer = new Customers(customerId, fullname, email, address, phoneNumber, null, postalCode);
                    if (salesRepId != 0) {
                        User user = new User(salesRepId, rs.getString("email"), rs.getString("password"), rs.getString("role"));
                        customer = new Customers(customerId, fullname, email, address, phoneNumber, user, postalCode);
                    }
                    customerList.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return customerList;
    }
}