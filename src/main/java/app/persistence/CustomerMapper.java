package app.persistence;

import app.entities.Customer;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool pool) {
        CustomerMapper.connectionPool = pool;
    }

    // save customer return with auto generated id
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

            ps.setString(1, c.getFullname());
            ps.setString(2, c.getAddress());
            ps.setInt(3, c.getPostalCode());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhoneNumber());

            // user_id kan være null (ingen sælger tilknyttet endnu)
            if (c.getSalesRep() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, c.getSalesRep().getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rs.getInt("id");
                    Customer customer = new Customer(rs.getInt("id"),c.getFullname(), c.getEmail(),c.getAddress(),c.getPhoneNumber(),c.getSalesRep(),c.getPostalCode());
                    return customer;
                } else {
                    throw new DatabaseException("Could not register customer");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static List<Customer> getCustomersWithoutSalesRep(int userId) throws DatabaseException {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT " +
                "    c.*, " +
                "    o.*, " +
                "    u.id AS salesrep_id, " +
                "    u.email AS salesrep_email, " +
                "    u.password AS salesrep_password, " +
                "    u.role AS salesrep_role " +
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
                    String customerEmail = rs.getString("email"); // comes from c.*
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phone_number");
                    int postalCode = rs.getInt("postal_code");

                    Customer customer = new Customer(customerId, fullname, customerEmail, address, phoneNumber, null, postalCode);

                    int salesRepId = rs.getInt("salesrep_id");
                    if (salesRepId != 0) {
                        String salesRepEmail = rs.getString("salesrep_email");
                        String salesRepPassword = rs.getString("salesrep_password");
                        String salesRepRole = rs.getString("salesrep_role");

                        User salesRep = new User(salesRepId, salesRepEmail, salesRepPassword, salesRepRole, null);
                        customer.setSalesRep(salesRep);
                    }
                    customerList.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return customerList;
    }

    public static Customer getCustomerWithId(int userId) throws DatabaseException {
        String sql = "SELECT " +
                "    c.*, " +
                "    u.*, " +
                "    u.id AS salesrep_id, " +
                "    u.email AS salesrep_email, " +
                "    u.password AS salesrep_password, " +
                "    u.role AS salesrep_role " +
                "FROM customers c " +
                "LEFT JOIN users u ON c.user_id = u.id " +
                "WHERE c.id = ? ";

        try {
            Connection connec = connectionPool.getConnection();
            PreparedStatement ps = connec.prepareStatement(sql);
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String fullname = rs.getString("fullname");
                    String customerEmail = rs.getString("email"); // comes from c.*
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phone_number");
                    int postalCode = rs.getInt("postal_code");

                    Customer customer = new Customer(userId, fullname, customerEmail, address, phoneNumber, null, postalCode);

                    int salesRepId = rs.getInt("salesrep_id");
                    if (salesRepId != 0) {
                        String salesRepEmail = rs.getString("salesrep_email");
                        String salesRepPassword = rs.getString("salesrep_password");
                        String salesRepRole = rs.getString("salesrep_role");

                        User salesRep = new User(salesRepId, salesRepEmail, salesRepPassword, salesRepRole, null);
                        customer.setSalesRep(salesRep);
                    }
                    return customer;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return null;
    }

    public static void assignSalesRepToCostumer(int customerId, int salesRepId) throws DatabaseException {
        String sql = "UPDATE customers SET user_id = ? WHERE id = ?;";
        try {
            Connection connec = connectionPool.getConnection();
            PreparedStatement ps = connec.prepareStatement(sql);
            ps.setInt(1, salesRepId);
            ps.setInt(2, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}