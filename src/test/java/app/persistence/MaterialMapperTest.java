package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class MaterialMapperTest {

    @BeforeAll
    public static void beforeAll() {
        MaterialMapper.setConnectionPool(SetupDatabase.getConnectionPool());
        try {
            SetupDatabase.createTables();
        } catch (DatabaseException e) {
            fail(e.getMessage());
        }
    }

    @BeforeEach
    void beforeEach() {
        try {
            SetupDatabase.seedTables();
        } catch (DatabaseException e) {
            fail(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testConnection() {
        try (Connection connection = SetupDatabase.getConnectionPool().getConnection())  {
            assertNotNull(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMaterialById() throws DatabaseException {
        // Arrange
        Material expected = new Material(1, "97x97 mm. trykimp. Stolpe", "Stk.", 42.95);

        // Act
        Material actual = MaterialMapper.getMaterialById(expected.getId());

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getMaterialByDescription() throws DatabaseException {
        // Arrange
        Material expected = new Material(2, "45x195 mm. spærtræ ubh.", "Stk.", 45.95);

        // Act
        Material actual = MaterialMapper.getMaterialByDescription(expected.getDescription());

        // Assert
        assertEquals(expected, actual);
    }
}