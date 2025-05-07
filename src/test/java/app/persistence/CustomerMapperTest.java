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
    static void beforeAll() {
        CustomerMapper.setConnectionPool(SetupDatabase.getConnectionPool());
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
