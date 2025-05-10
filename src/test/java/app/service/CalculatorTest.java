package app.service;

import app.entities.Material;
import app.entities.Partslist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @BeforeAll
    public static void beforeAll() {
    }

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculateEightPosts() {
        // Arrange
        int carportLength = 660;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 8;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        PostCalculationStrategy calculator = new PostCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateSixPostsUpperBoundary() {
        // Arrange
        int carportLength = 630;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 6;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        PostCalculationStrategy calculator = new PostCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateSixPostsLowerBoundary() {
        // Arrange
        int carportLength = 450;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 6;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        PostCalculationStrategy calculator = new PostCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateFourPosts() {
        // Arrange
        int carportLength = 420;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfPosts = 4;
        int expectedLengthOfPosts = 300;
        Material material = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        PostCalculationStrategy calculator = new PostCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfPosts, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfPosts, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateOneBeam() {
        // Arrange
        int carportLength = 300;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfBeams = 1;
        int expectedLengthOfBeams = 600;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        BeamCalculationStrategy calculator = new BeamCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateTwoBeamsLowerBoundary() {
        // Arrange
        int carportLength = 330;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfBeams = 2;
        int expectedLengthOfBeams = 360;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        BeamCalculationStrategy calculator = new BeamCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateTwoBeamsUpperBoundary() {
        // Arrange
        int carportLength = 600;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfBeams = 2;
        int expectedLengthOfBeams = 600;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        BeamCalculationStrategy calculator = new BeamCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfBeams, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfBeams, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateThreeBeams() {
        // Arrange
        int carportLength = 630;
        int carportWidth = 600;
        int partslistSize = 2;
        int expectedNumberOfBeams1 = 2;
        int expectedLengthOfBeams1 = 600;
        int expectedNumberOfBeams2 = 1;
        int expectedLengthOfBeams2 = 360;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        BeamCalculationStrategy calculator = new BeamCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

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
    void calculateSmallestRafters() {
        // Arrange
        int carportLength = 240;
        int carportWidth = 240;
        int partslistSize = 1;
        int expectedNumberOfRafters = 5;
        int expectedLengthOfRafters = carportWidth;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        RaftersCalculationStrategy calculator = new RaftersCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfRafters, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfRafters, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void calculateBiggestRafters() {
        // Arrange
        int carportLength = 780;
        int carportWidth = 600;
        int partslistSize = 1;
        int expectedNumberOfRafters = 15;
        int expectedLengthOfRafters = carportWidth;
        Material material = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        RaftersCalculationStrategy calculator = new RaftersCalculationStrategy();

        // Act
        List<Partslist> partslist =  calculator.calculate(carportLength, carportWidth, material);

        // Assert
        assertEquals(partslistSize, partslist.size());
        assertEquals(expectedNumberOfRafters, partslist.get(0).getQuantity());
        assertEquals(expectedLengthOfRafters, partslist.get(0).getLength());
        assertEquals(material, partslist.get(0).getMaterial());
    }

    @Test
    void getPartslists() {
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
        Material rafter = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);
        calculator.addStrategy(new PostCalculationStrategy(), post);
        calculator.addStrategy(new BeamCalculationStrategy(), beam);
        calculator.addStrategy(new RaftersCalculationStrategy(), rafter);

        // Act
        List<Partslist> partslist =  calculator.getPartslists();

        // Assert
        assertEquals(partslistSize, partslist.size());

        Partslist posts = partslist.stream()
                .filter(x -> "Stolper nedgraves 90 cm. i jord".equals(x.getDescription()))
                .findFirst()
                .orElse(null);

        Partslist beams = partslist.stream()
                .filter(x -> "Remme i sider, sadles ned i stolper".equals(x.getDescription()))
                .findFirst()
                .orElse(null);

        Partslist beamsSawed = partslist.stream()
                .filter(x -> "Remme i sider, sadles ned i stolper (deles)".equals(x.getDescription()))
                .findFirst()
                .orElse(null);

        Partslist rafters = partslist.stream()
                .filter(x -> "Spær, monteres på rem".equals(x.getDescription()))
                .findFirst()
                .orElse(null);

        assertNotNull(posts);
        assertEquals(postLength, posts.getLength());
        assertEquals(postQuantity, posts.getQuantity());
        assertEquals(post, posts.getMaterial());

        assertNotNull(beams);
        assertEquals(beamLength1, beams.getLength());
        assertEquals(beamQuantity1, beams.getQuantity());
        assertEquals(beam, beams.getMaterial());

        assertNotNull(beamsSawed);
        assertEquals(beamLength2, beamsSawed.getLength());
        assertEquals(beamQuantity2, beamsSawed.getQuantity());
        assertEquals(beam, beamsSawed.getMaterial());

        assertNotNull(rafters);
        assertEquals(rafterLength, rafters.getLength());
        assertEquals(rafterQuantity,rafters.getQuantity());
        assertEquals(rafter, rafters.getMaterial());
    }

    @Test
    void getTotalPriceOfSmallestCarport() {
        // Arrange
        int carportLength = 240;
        int carportWidth = 240;
        double expectedPrice = 1287.36;
        Material post = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Material beam = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Material rafters = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);
        calculator.addStrategy(new PostCalculationStrategy(), post);
        calculator.addStrategy(new BeamCalculationStrategy(), beam);
        calculator.addStrategy(new RaftersCalculationStrategy(), rafters);

        // Act
        calculator.getPartslists();
        double actualPrice = calculator.getTotalPrice();

        // Assert
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void getTotalPriceOfBiggestCarport() {
        // Arrange
        int carportLength = 780;
        int carportWidth = 600;
        double expectedPrice = 5883.12;
        Material post = new Material("97x97 mm. trykimp. Stolpe", "Stk.", 42.95);
        Material beam = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Material rafters = new Material("45x195 mm. spærtræ ubh.", "Stk.", 45.95);
        Calculator calculator = new Calculator(carportLength, carportWidth);
        calculator.addStrategy(new PostCalculationStrategy(), post);
        calculator.addStrategy(new BeamCalculationStrategy(), beam);
        calculator.addStrategy(new RaftersCalculationStrategy(), rafters);

        // Act
        calculator.getPartslists();
        double actualPrice = calculator.getTotalPrice();

        // Assert
        assertEquals(expectedPrice, actualPrice);
    }
}