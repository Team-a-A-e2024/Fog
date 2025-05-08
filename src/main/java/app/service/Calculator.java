package app.service;

import app.entities.Material;
import app.entities.Partslist;
import app.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private int length;
    private int width;

    // Post requirements
    private int postLength = 300;
    private int firstPost = 100;
    private int lastPost = length-30;
    private int maxDistanceBetweenPosts = 310;
    private int postAtBeamExtension = 600;
    private Integer firstExtraPost = null;
    private Integer secondExtraPost = null;

    // Beam requirements
    private int maxBeamLength = 600;
    private int minBeamLength = 360;
    private Integer firstBeamLength = null;
    private Integer secondBeamLength = null;
    private int numberOfFirstBeams = 0;
    private int numberOfSecondBeams = 0;

    // Rafters requirements
    private int rafterSpacing = 55;
    private int rafterLength = width;

    private List<Partslist> partslists;

    public Calculator(int length, int width) {
        this.length = length;
        this.width = width;
        partslists = new ArrayList<>();
    }

    public List<Partslist> calculatePosts(Material material) throws DatabaseException {
        partslists.add(new Partslist(
                    calculateNumberOfPosts(),
                    "Stolper nedgraves 90 cm. i jord",
                    postLength,
                    material
        ));

        return partslists;
    }

    public List<Partslist> calculateBeams(Material material) throws DatabaseException {
        partslists.add(new Partslist(
                calculateNumberOfFirstBeams(),
                "Remme i sider, sadles ned i stolper",
                calculateFirstBeamLength(),
                material
        ));

        if (calculateNumberOfSecondBeams() > 0) {
            partslists.add(new Partslist(
                    calculateNumberOfSecondBeams(),
                    "Remme i sider, sadles ned i stolper (deles)",
                    calculateSecondBeamLength(),
                    material
            ));
        }

        return partslists;
    }

    public List<Partslist> calculateRafters(Material material) throws DatabaseException {
        partslists.add(new Partslist(
                calculateNumberOfRafters(),
                "Spær, monteres på rem",
                calculateRaftersLength(),
                material
        ));

        return partslists;
    }

    public List<Partslist> getPartslists(Material posts, Material beams, Material rafters) throws DatabaseException {
        partslists = new ArrayList<>();
        calculatePosts(posts);
        calculateBeams(beams);
        calculateRafters(rafters);
        return partslists;
    }

    public double getTotalPrice() {
        double totalPrice = 0;

        for (Partslist part : partslists) {
            totalPrice += ((double)part.getLength() * (double)part.getQuantity()) / 100d * part.getMaterial().getPrice();
        }

        return (double) Math.round(totalPrice * 100) / 100;
    }

    private int calculateNumberOfPosts() {
        if (!addFirstExtraPost()) {
            return 4;
        }
        else if (!addSecondExtraPost()) {
            return 6;
        }
        else {
            return 8;
        }
    }

    private int distanceBetweenFirstAndSecondPost() {
        if (addSecondExtraPost()) {
            return secondExtraPost-firstPost;
        }
        else {
            return lastPost-firstPost;
        }
    }

    private boolean addFirstExtraPost() {
        if (distanceBetweenFirstAndSecondPost() > maxDistanceBetweenPosts) {
            calculateFirstExtraPostLength();
            return true;
        }

        return false;
    }

    private boolean addSecondExtraPost() {
        lastPost = length-30;
        if (lastPost > postAtBeamExtension) {
            calculateSecondExtraPostLength();
            return true;
        }

        return false;
    }

    private void calculateFirstExtraPostLength() {
        firstExtraPost = distanceBetweenFirstAndSecondPost() / 2;
    }

    private void calculateSecondExtraPostLength() {
        secondExtraPost = postAtBeamExtension;

    }

    private int calculateNumberOfFirstBeams() {
        if (length <= maxBeamLength/2) {
            numberOfFirstBeams = 1;
        }
        else {
            numberOfFirstBeams = 2;
        }

        return numberOfFirstBeams;
    }

    private int calculateNumberOfSecondBeams() {
        if (length > maxBeamLength) {
            numberOfSecondBeams = 1;
        }
        else {
            numberOfSecondBeams = 0;
        }

        return numberOfSecondBeams;
    }

    private int calculateFirstBeamLength() {
        if (length > maxBeamLength) {
            firstBeamLength = maxBeamLength;
        }
        else if (length >= minBeamLength) {
            firstBeamLength = length;
        }
        else if (length > maxBeamLength/2) {
            firstBeamLength = minBeamLength;
        }
        else {
            firstBeamLength = length * 2;
        }

        return firstBeamLength;
    }

    private int calculateSecondBeamLength() {
        if (numberOfSecondBeams == 1) {
            secondBeamLength = minBeamLength;
        }

        return secondBeamLength;
    }

    private int calculateNumberOfRafters() {
        return (int) Math.ceil((double)length / (double)rafterSpacing);
    }

    private int calculateRaftersLength() {
        return width;
    }
}
