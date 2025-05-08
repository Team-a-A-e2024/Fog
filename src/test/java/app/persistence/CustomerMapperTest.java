package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class CustomerMapperTest {

    @BeforeAll
    public static void beforeAll() {
        CustomerMapper.setConnectionPool(SetupDatabase.getConnectionPool());
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
        List<Customer> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        assertNotNull(result);
        assertEquals(3, result.size());

        List<String> names = result.stream().map(Customer::getFullName).toList();

        assertTrue(names.contains("Customer1"));
        assertTrue(names.contains("Customer3"));
        assertTrue(names.contains("Customer4"));
    }

    @Test
    void saveCustomers() throws DatabaseException {
        // arrange
        Customer in = new Customer();
        in.setFullName("Peter Nielsen");
        in.setAddress("Theisvej 7");
        in.setPostalCode(2300);
        in.setEmail("peter@nielsen.dk");
        in.setPhoneNumber("22331122");
        in.setUserId(1);

        //act
        Customer out = CustomerMapper.save(in);

        // assert
        assertTrue(in.getId() > 0);
        assertEquals("Peter Nielsen", out.getFullName());
        assertEquals("Theisvej 7", out.getAddress());
        assertEquals(2300, out.getPostalCode());
        assertEquals("peter@nielsen.dk", out.getEmail());
        assertEquals("22331122", out.getPhoneNumber());
    }
}
