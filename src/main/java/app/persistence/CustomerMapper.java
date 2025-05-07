package app.persistence;

import app.entities.Customers;
import app.exceptions.DatabaseException;

import java.sql.*;
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

    public static List<Customers> getCustomersWithoutSalesRep(int userId) throws DatabaseException {
        List<Customers> customerList = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* " +
                "FROM customers c " +
                "LEFT JOIN orders o ON c.id = o.customer_id " +
                "WHERE c.user_id IS NULL " +
                "OR (c.user_id = ? AND o.status != 'Afventer')";
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
                    int assignedSalesRepId = rs.getInt("user_id");
                    int postalCode = rs.getInt("postal_code");

                    Customers customer = new Customers(customerId, fullname, email, address, phoneNumber, assignedSalesRepId, postalCode);
                    customerList.add(customer);
                }
                if (rs.next()) c.setId(rs.getInt("id"));
                else throw new DatabaseException("Could not register customer");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return c;
        return customerList;
    }
}