package app.persistence;

import app.entities.Material;
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

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");

                return new Partslist(id, part.getQuantity(), part.getDescription(), part.getLength(), part.getMaterial());
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
    public static List<Partslist> getPaidPartslist(int orderId) throws DatabaseException {
        List<Partslist> partslists = new ArrayList<>();

        String sql = "SELECT " +
                "m.description AS material, " +
                "m.id AS material_id, " +
                "m.price AS material_price, " +
                "p.length, " +
                "p.id, " +
                "p.quantity, " +
                "m.unit, " +
                "p.description " +
                "FROM partslist p " +
                "JOIN orders o ON p.order_id = o.id " +
                "JOIN materials m ON m.id = p.material_id " +
                "WHERE o.status IN ('Godkendt', 'Leveret', 'Afsendt') AND o.id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int partId = rs.getInt("id");
                    String materialDescription = rs.getString("material");
                    int length = rs.getInt("length");
                    int quantity = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    String partslistDescription = rs.getString("description");
                    int materialId = rs.getInt("material_id");
                    double materialPrice = rs.getDouble("material_price");

                    Material material = new Material(materialId ,materialDescription, unit, materialPrice);

                    Partslist partslist = new Partslist(partId, quantity, partslistDescription, length, material);

                    partslists.add(partslist);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching partslist", e.getMessage());
        }
        return partslists;
    }
}