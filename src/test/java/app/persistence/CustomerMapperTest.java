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
    void returnUser() throws DatabaseException {
        int userId = 1; // skal matche en kunde i testdata
        List<Customers> result = CustomerMapper.getCustomersWithoutSalesRep(userId);
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Forventer at få mindst én kunde");
    }

    @Test
    void noUserReturned() throws DatabaseException {
        int userId = 999; // bruger ID der ikke har nogen kunder
        List<Customers> result = CustomerMapper.getCustomersWithoutSalesRep(userId);
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Forventer tom liste for userId 999");
    }
}
