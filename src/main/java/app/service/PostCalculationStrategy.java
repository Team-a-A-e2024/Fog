package app.service;

import app.entities.Material;
import app.entities.Partslist;

import java.util.ArrayList;
import java.util.List;

public class PostCalculationStrategy implements ICalculationStrategy {
    private int length;
    private int postLength = 300;
    private int firstPost = 100;
    private int maxDistanceBetweenPosts = 310;
    private int postAtBeamExtension = 600;

    @Override
    public List<Partslist> calculate(int length, int width, Material material) {
        this.length = length;

        List<Partslist> partslists = new ArrayList<>();

        partslists.add(new Partslist(
                calculateNumberOfPosts(),
                "Stolper nedgraves 90 cm. i jord",
                postLength,
                material
        ));

        return partslists;
    }

    /**
     * Calculates the number of posts needed to build the carport.
     * @return An integer value of either 4, 6 or 8.
     */
    private int calculateNumberOfPosts() {
        int lastPost = length-30;
        int delta = lastPost-firstPost;

        if (lastPost > postAtBeamExtension) return 8;

        return (delta > maxDistanceBetweenPosts) ? 6 : 4;
    }
}
