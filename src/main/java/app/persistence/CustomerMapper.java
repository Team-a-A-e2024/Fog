package app.persistence;

import app.entities.Customer;
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

            ps.setString(1, c.getFullName());
            ps.setString(2, c.getAddress());
            ps.setInt   (3, c.getPostalCode());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPhoneNumber());

            // user_id kan være null (ingen sælger tilknyttet endnu)
            if (c.getUserId() == 0) ps.setNull(6, Types.INTEGER);
            else                     ps.setInt (6, c.getUserId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c.setId(rs.getInt("id"));              // PK sat tilbage
                } else {
                    throw new DatabaseException("Could not register customer");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return c;
    }

    //   Hent alle kunder uden sælger – eller med ’mine’ uafsluttede sager
    public static List<Customer> getCustomersWithoutSalesRep(int userId)
            throws DatabaseException {

        final String sql = """
                SELECT DISTINCT c.*
                FROM   customers c
                LEFT   JOIN orders o ON c.id = o.customer_id
                WHERE  c.user_id IS NULL
                   OR (c.user_id = ? AND o.status <> 'Afventer')
        """;

        List<Customer> customerList = new ArrayList<>();

        try (Connection con = connectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int customerId   = rs.getInt("id");
                    String fullname  = rs.getString("fullname");
                    String address   = rs.getString("address");
                    int postalCode   = rs.getInt("postal_code");
                    String email     = rs.getString("email");
                    String phone     = rs.getString("phone_number");
                    int assignedUser = rs.getInt("user_id");

                    customerList.add(new Customer(
                            customerId,
                            fullname,
                            address,
                            postalCode,
                            email,
                            phone,
                            assignedUser
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return customerList;
    }
}
