package app.persistence;

import app.exceptions.DatabaseException;
import app.entities.User;
import app.util.PasswordUtil;
import org.junit.jupiter.api.*;
import app.test.SetupDatabase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class UserMapperTest {

    @BeforeAll
    public static void beforeAll() {
        UserMapper.setConnectionPool(SetupDatabase.getConnectionPool());
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
    void createUser() throws DatabaseException {
        // Arrange
        User expected = new User(6, "test", "test", "SalesRep", new Date(12,12,12));

        // Act
        User actual = UserMapper.addUserByObject(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getEmailValidUser() throws DatabaseException {
        String email = "test1";

        User user = UserMapper.getUserByEmail(email);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("Test1", user.getPassword());
        assertEquals("SalesRep", user.getRole());
    }

    @Test
    void getEmailInvalidUser() throws DatabaseException {
        String email = "fake";

        User user = UserMapper.getUserByEmail(email);

        assertNull(user);
    }

    @Test
    void testLoginValidUser() throws DatabaseException {
        String email = "test1";
        String password = "Test1";

        User user = UserMapper.getUserByEmail(email);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("SalesRep", user.getRole());
        assertTrue(PasswordUtil.checkPlainPassword(password,user.getPassword()));
    }

    @Test
    void testLoginWrongPassword() throws DatabaseException {
        String email = "admin";
        String wrongPassword = "WrongPass";

        User user = UserMapper.getUserByEmail(email);

        assertFalse(PasswordUtil.checkPlainPassword(wrongPassword,user.getPassword()));
    }

    @Test
    void testLoginUnknownUser() throws DatabaseException {
        String email = "Fake";

        User user = UserMapper.getUserByEmail(email);

        assertNull(user);
    }

    @Test
    void updateUser() throws DatabaseException {
        //arrange
        String email = "updated";
        User expected = new User(1, email, "huwdhuaiw", "wdjkawdb hkjlb", new Date(2312,2,12));

        UserMapper.updateUserByObject(expected);
        User actual = UserMapper.getUserByEmail(email);

        assertEquals(expected,actual);

    }

}