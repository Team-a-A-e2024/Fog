package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool connectionPool) {
        MaterialMapper.connectionPool = connectionPool;
    }

    public static Material getMaterialById(int id) throws DatabaseException {
        String sql = "SELECT *" +
                "FROM materials WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Material(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("unit"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return null;
    }

    public static Material getMaterialByDescription(String description) throws DatabaseException {
        String sql = "SELECT *" +
                "FROM materials WHERE description = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, description);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Material(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("unit"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return null;
    }
}
