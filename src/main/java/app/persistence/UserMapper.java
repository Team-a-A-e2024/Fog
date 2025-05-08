package app.persistence;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool connectionPool) {
        UserMapper.connectionPool = connectionPool;
    }

    public static User addUserByObject(User user) throws DatabaseException
    {
        String sql = "insert into users (email,password,role,password_changed_date) VALUES (?,?,?,?) returning id ;";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1,user.getEmail());
                ps.setString(2,user.getPassword());
                ps.setString(3,user.getRole());
                ps.setDate(4,user.getPasswordChangeDate());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");

                        return new User(id, user.getEmail(), user.getPassword(), user.getRole(), user.getPasswordChangeDate());
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

    public static User getUserByEmail(String email) throws DatabaseException{
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), rs.getString("role"), rs.getDate("password_changed_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return null;
    }

    public static void updateUserByObject(User user) throws DatabaseException
    {
        String sql = "UPDATE users SET email = ?,password = ?,role = ?,password_changed_date = ? WHERE id = ? ;";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1,user.getEmail());
                ps.setString(2,user.getPassword());
                ps.setString(3,user.getRole());
                ps.setDate(4,user.getPasswordChangeDate());
                ps.setInt(5,user.getId());
                ps.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
}