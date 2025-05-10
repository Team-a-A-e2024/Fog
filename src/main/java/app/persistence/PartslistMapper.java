package app.persistence;

import app.entities.Partslist;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartslistMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool connectionPool) {
        PartslistMapper.connectionPool = connectionPool;
    }

    public static Partslist createPartslist(Partslist part, int orderId) throws DatabaseException {
        String sql = "INSERT INTO partslist (order_id, material_id, quantity, description, length) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, part.getMaterial().getId());
            ps.setInt(3, part.getQuantity());
            ps.setString(4, part.getDescription());
            ps.setInt(5, part.getLength());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");

                    return new Partslist(id, part.getQuantity(), part.getDescription(), part.getLength(), part.getMaterial());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }

        return null;
    }

    public static List<Partslist> getPartslistByOrderId(int orderId) throws DatabaseException {
        List<Partslist> partslists = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM partslist " +
                "WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                partslists.add(new Partslist(
                        rs.getInt("id"),
                        rs.getInt("quantity"),
                        rs.getString("description"),
                        rs.getInt("length"),
                        MaterialMapper.getMaterialById(rs.getInt("material_id"))
                ));
            }

            return partslists;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
    }
}
