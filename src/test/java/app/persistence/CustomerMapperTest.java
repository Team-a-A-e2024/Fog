package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

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
        void saveCustomers () throws DatabaseException {
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

            // Make sure Customer2 is not in the list
            for (Customers customer : result) {
                assertNotEquals("Customer2", customer.getFullname());
            }
            //assert
            assertTrue(out.getId() > 0, "ID skal sættes af DB");

            assertEquals(in.getFullName(), out.getFullName());
            assertEquals(in.getAddress(), out.getAddress());
            assertEquals(in.getPostalCode(), out.getPostalCode());
            assertEquals(in.getEmail(), out.getEmail());
            assertEquals(in.getPhoneNumber(), out.getPhoneNumber());
            assertEquals(in.getUserId(), out.getUserId());
        }
    }
