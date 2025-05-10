package app.persistence;

import app.entities.Material;
import app.entities.Partslist;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class PartslistMapperTest {

    @BeforeAll
    public static void beforeAll() {
        MaterialMapper.setConnectionPool(SetupDatabase.getConnectionPool());
        PartslistMapper.setConnectionPool(SetupDatabase.getConnectionPool());
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
    void testConnection() throws SQLException {
        assertNotNull(SetupDatabase.getConnectionPool().getConnection());
    }

    @Test
    void createPartslist() throws DatabaseException {
        // Arrange
        Material post = new Material(1, "97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Partslist expected = new Partslist(2, 2, "Free posts", 450, post);

        // Act
        Partslist actual = PartslistMapper.createPartslist(expected, 1);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getPartslistByOrderId() throws DatabaseException {
        // Arrange
        Material post = new Material(1, "97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        List<Partslist> expected = new ArrayList<>();
        expected.add(new Partslist(1, 1, "Test wood", 600, post));

        // Act
        List<Partslist> actual = PartslistMapper.getPartslistByOrderId(1);

        // Assert
        assertEquals(expected, actual);
    }
}