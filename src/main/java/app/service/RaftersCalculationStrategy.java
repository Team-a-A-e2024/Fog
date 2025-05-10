package app.service;

import app.entities.Material;
import app.entities.Partslist;

import java.util.ArrayList;
import java.util.List;

public class RaftersCalculationStrategy implements ICalculationStrategy {
    private int length;
    private int rafterSpacing = 55;

    @Override
    public List<Partslist> calculate(int length, int width, Material material) {
        this.length = length;

        List<Partslist> partslists = new ArrayList<>();

        partslists.add(new Partslist(
                calculateNumberOfRafters(),
                "Spær, monteres på rem",
                width,
                material
        ));

        return partslists;
    }

    /**
     * Calculates the number of rafters needed to build the carport.
     * @return An integer value depending on the length.
     */
    private int calculateNumberOfRafters() {
        return (int) Math.ceil((double)length / (double)rafterSpacing);
    }
}
