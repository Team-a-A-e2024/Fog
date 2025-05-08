package app.service;

import app.entities.Material;
import app.entities.Partslist;
import app.exceptions.DatabaseException;
import app.persistence.MaterialMapper;
import app.persistence.UserMapper;
import app.test.SetupDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @BeforeAll
    public static void beforeAll() {
        MaterialMapper.setConnectionPool(SetupDatabase.getConnectionPool());
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
    void calculateEightPosts() throws DatabaseException {
        // Arrange
        int carportLength = 660;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 8;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculatePosts(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateSixPostsUpperBoundary() throws DatabaseException {
        // Arrange
        int carportLength = 630;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 6;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculatePosts(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateSixPostsLowerBoundary() throws DatabaseException {
        // Arrange
        int carportLength = 450;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 6;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculatePosts(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateFourPosts() throws DatabaseException {
        // Arrange
        int carportLength = 420;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 4;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculatePosts(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateOneBeam() throws DatabaseException {
        // Arrange
        int carportLength = 300;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfBeams = 1;
        int expectedLengthOfBeams = 600;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculateBeams(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateTwoBeamsLowerBoundary() throws DatabaseException {
        // Arrange
        int carportLength = 330;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfBeams = 2;
        int expectedLengthOfBeams = 360;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculateBeams(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateTwoBeamsUpperBoundary() throws DatabaseException {
        // Arrange
        int carportLength = 600;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfBeams = 2;
        int expectedLengthOfBeams = 600;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculateBeams(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateThreeBeams() throws DatabaseException {
        // Arrange
        int carportLength = 630;
        int carportWidth = 600;
        int partslistSize = 2;
        int expectedNumberOfBeams1 = 2;
        int expectedLengthOfBeams1 = 600;
        int expectedNumberOfBeams2 = 1;
        int expectedLengthOfBeams2 = 360;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculateBeams(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams1, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams1, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
        assertEquals(expectedNumberOfBeams2, partslist.get(1).getQuantity());
        assertEquals(expectedLengthOfBeams2, partslist.get(1).getLength());
        assertEquals(material, partslist.get(1).getMaterial());
    }

    @Test
    void calculateSmallestRafters() throws DatabaseException {
        // Arrange
        int carportLength = 240;
        int carportWidth = 240;
        int partslistSize = 1;
        int expectedNumberOfRafters = 5;
        int expectedLengthOfRafters = carportWidth;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculateRafters(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfRafters, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfRafters, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateBiggestRafters() throws DatabaseException {
        // Arrange
        int carportLength = 780;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfRafters = 15;
        int expectedLengthOfRafters = carportWidth;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.calculateRafters(material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfRafters, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfRafters, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void getPartslists() throws DatabaseException {
        // Arrange
        int carportLength = 780;
        int carportWidth = 600;
        int partslistSize = 4;
        int postLength = 300;
        int postQuantity = 8;
        int beamLength1 = 600;
        int beamQuantity1 = 2;
        int beamLength2 = 360;
        int beamQuantity2 = 1;
        int rafterLength = 600;
        int rafterQuantity = 15;
        Material post = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Material beam = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Material rafters = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        List<Partslist> partslist =  calculator.getPartslists(post, beam, rafters);

        // Assert
        assertEquals(partslistSize, partslist.size());

        assertEquals(postLength, partslist.get(0).getLength());
        assertEquals(postQuantity, partslist.get(0).getQuantity());
        assertEquals(post, partslist.get(0).getMaterial());

        assertEquals(beamLength1, partslist.get(1).getLength());
        assertEquals(beamQuantity1, partslist.get(1).getQuantity());
        assertEquals(beam, partslist.get(1).getMaterial());

        assertEquals(beamLength2, partslist.get(2).getLength());
        assertEquals(beamQuantity2, partslist.get(2).getQuantity());
        assertEquals(beam, partslist.get(2).getMaterial());

        assertEquals(rafterLength, partslist.get(3).getLength());
        assertEquals(rafterQuantity, partslist.get(3).getQuantity());
        assertEquals(rafters, partslist.get(3).getMaterial());
    }

    @Test
    void getTotalPriceOfSmallestCarport() throws DatabaseException {
        // Arrange
        int carportLength = 240;
        int carportWidth = 240;
        double expectedPrice = 1287.36;
        Material post = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Material beam = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Material rafters = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        calculator.getPartslists(post, beam, rafters);
        double actualPrice = calculator.getTotalPrice();

        // Assert
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void getTotalPriceOfBiggestCarport() throws DatabaseException {
        // Arrange
        int carportLength = 780;
        int carportWidth = 600;
        double expectedPrice = 5883.12;
        Material post = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Material beam = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Material rafters = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);

        // Act
        calculator.getPartslists(post, beam, rafters);
        double actualPrice = calculator.getTotalPrice();

        // Assert
        assertEquals(expectedPrice, actualPrice);
    }
}