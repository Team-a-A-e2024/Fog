package app.persistence;

import app.entities.Customers;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class CustomerMapperTest {

    @BeforeAll
    public static void beforeAll() {
        CustomerMapper.SetConnectionPool(SetupDatabase.getConnectionPool());
        try {
            SetupDatabase.createTables();
        } catch (DatabaseException e) {
            fail("Fejl i createTables: " + e.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        try {
            SetupDatabase.seedTables();
        } catch (DatabaseException e) {
            fail("Fejl ved seedTables: " + e.getMessage());
        }
    }

    @Test
    void getCustomersWithoutSalesRep_shouldReturnExpectedCustomers() throws DatabaseException {
        // Test: should return customers where user_id is null OR user_id = 2 AND order status is not 'Afventer'
        int userId = 2;
        List<Customers> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        assertNotNull(result);
        assertEquals(3, result.size());

        List<String> names = result.stream().map(Customers::getFullname).toList();

        assertTrue(names.contains("Customer1"));
        assertTrue(names.contains("Customer3"));
        assertTrue(names.contains("Customer4"));
    }

    @Test
    void customerWithAfventerOrder_shouldNotBeIncluded() throws DatabaseException {
        // Test: Customer2 has user_id = 3 and status = 'Afventer', so should NOT be included
        int userId = 3;
        List<Customers> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        // Make sure Customer2 is not in the list
        for (Customers customer : result) {
            assertNotEquals("Customer2", customer.getFullname());
        }
    }
}