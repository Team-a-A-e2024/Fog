package app.persistence;

import app.entities.Customer;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.test.SetupDatabase;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    @BeforeAll
    static void setupConnection() {
        CustomerMapper.SetConnectionPool(SetupDatabase.getConnectionPool());
        try {
            SetupDatabase.createTables();
        } catch (DatabaseException e) {
            fail("Kunne ikke oprette testtabeller");
        }
    }

    @BeforeEach
    void seed() {
        try {
            SetupDatabase.seedTables();
        } catch (DatabaseException e) {
            fail("Kunne ikke indsætte testdata");
        }
    }

    @Test
    //Test if customers without a sales representative (user_id = null) are returned correctly
    void testCustomersWithoutSalesRep() throws DatabaseException {
        int userId = 2;
        List<Customer> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        for (Customer c : result) {
            if (c.getFullname().equals("Customer4")) {
                assertNull(c.getSalesRep(), "Customer4 should not have a sales rep assigned");
            }
        }
    }

    @Test
    // Test if only customers with an 'Afventer' order status are included
    void testOnlyAfventerStatus() throws DatabaseException {
        int userId = 2;
        List<Customer> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        for (Customer c : result) {
            assertTrue(
                    c.getFullname().equals("Customer1") || c.getFullname().equals("Customer4"),
                    "Only customers with 'Afventer' orders should be included"
            );
        }
    }

    @Test
    // Test that customers with non-'Afventer' status are not included in the result
    void testCustomersWithNonAfventerStatus() throws DatabaseException {
        int userId = 3;
        List<Customer> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        for (Customer c : result) {
            assertNotEquals("Customer2", c.getFullname(), "Customer2 has status 'Godkendt' and should not be included");
        }
    }

    @Test
    //Test that customers with a sales representative assigned include the correct salesRep object
    void testCustomerHasSalesRepAssigned() throws DatabaseException {
        int userId = 2;
        List<Customer> result = CustomerMapper.getCustomersWithoutSalesRep(userId);

        for (Customer c : result) {
            if (c.getFullname().equals("Customer1")) {
                User rep = c.getSalesRep();
                assertNotNull(rep, "Customer1 should have a sales rep");
                assertEquals(2, rep.getId(), "Customer1 should be assigned to user with id 2");
            }
        }
    }

    @Test
    // Test that assigning a sales representative to a customer updates the user_id correctly
    void testAssignSalesRepToCustomer() throws DatabaseException {
        int customerId = 4; // Customer4 har user_id = null
        int salesRepId = 2;

        CustomerMapper.assignSalesRepToCostumer(customerId, salesRepId);

        List<Customer> result = CustomerMapper.getCustomersWithoutSalesRep(salesRepId);

        boolean found = false;

        for (Customer c : result) {
            if (c.getId() == customerId && c.getSalesRep() != null && c.getSalesRep().getId() == salesRepId) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Customer4 blev ikke opdateret med user_id = 2");
    }
}