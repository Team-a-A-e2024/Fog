package app.persistence;

import app.entities.Order;                // ← importér Order!
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class OrderMapperTest {

    @BeforeAll
    static void beforeAll() {
        var pool = SetupDatabase.getConnectionPool();
        OrderMapper.setConnectionPool(pool);
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

    @Test
    void connectionPoolWorks() throws SQLException {
        assertNotNull(SetupDatabase.getConnectionPool().getConnection());
    }

    @Test
    void saveOrder() throws DatabaseException {
        // arrange
        Order in = new Order();
        in.setCustomerId(1);
        in.setWidthCm(240);
        in.setLengthCm(360);
        in.setComments("fast delivery");
        in.setTotal(45.0);
        in.setStatus("awaiting");
        in.setCreatedAt(LocalDateTime.now());

        // act
        Order out = OrderMapper.toOrderAndSave(in);

        // assert
        assertTrue(out.getId() > 0);
        assertEquals(1, out.getCustomerId());
        assertEquals(240, out.getWidthCm());
        assertEquals(360, out.getLengthCm());
        assertEquals("fast delivery", out.getComments());
        assertEquals("awaiting", out.getStatus());
        assertEquals(45.0, out.getTotal());
    }
}


