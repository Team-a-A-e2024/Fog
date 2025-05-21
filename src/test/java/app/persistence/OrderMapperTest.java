package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        Order expected = new Order(
                5,
                1,
                45.0,
                "awaiting",
                240,
                360,
                "fast delivery",
                LocalDateTime.now()
        );

        // act
        Order actual = OrderMapper.toOrderAndSave(expected);

        // assert
        assertEquals(expected,actual);
    }

    @Test
    void updateTotal() throws DatabaseException {
        // Arrange
        int expected = 1;
        int totalPrice = 9999;
        Order order = new Order(
                1,
                1,
                totalPrice,
                "Godkendt",
                10 ,
                10,
                "Ordre til Customer1",
                LocalDateTime.now()
        );

        // Act
         int actual = OrderMapper.updateTotalByOrderId(order.getId(), totalPrice);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void updateOrder() throws DatabaseException {
        // Arrange
        Order expected = new Order(
                1,
                1,
                9999,
                "Godkendt",
                10 ,
                10,
                "Ordre til Customer1",
                LocalDate.now().atStartOfDay()
        );

        // Act
        OrderMapper.updateOrderByObject(expected);
        Order actual = OrderMapper.getOrderByid(expected.getId());

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getListOfOrders() throws DatabaseException {
        // Arrange
        List<Order> expected = new ArrayList<>();
        expected.add(new Order(1,1,1000,"Godkendt",10,10,"Ordre til Customer1",(LocalDate.of(2025,1,1)).atStartOfDay()));
        expected.add(new Order(2,2,1000,"Godkendt",10,10,"Ordre til Customer2",(LocalDate.of(2025,1,2)).atStartOfDay()));
        expected.add(new Order(3,4,1000,"Afventer",10,10,"Ordre til Customer4",(LocalDate.of(2025,1,3)).atStartOfDay()));

        // Act
        List<Order> actual = OrderMapper.getListofOrders();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void updateStatus() throws DatabaseException {
        // Arrange
        String status = "Annulleret";
        Order expected = new Order(
                1,
                1,
                1000,
                status,
                10 ,
                10,
                "Ordre til Customer1",
                LocalDateTime.parse("2025-01-01 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        // Act
        Order actual = OrderMapper.updateStatusByOrderId(expected.getId(), status);

        // Assert
        assertEquals(expected, actual);
    }
}


