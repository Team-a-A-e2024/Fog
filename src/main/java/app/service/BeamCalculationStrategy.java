package app.service;

import app.entities.Material;
import app.entities.Partslist;
import java.util.ArrayList;
import java.util.List;

public class BeamCalculationStrategy implements ICalculationStrategy {
    private int length;
    private int maxBeamLength = 600;
    private int minBeamLength = 360;

    @Override
    public List<Partslist> calculate(int length, int width, Material material)  {
        this.length = length;

        List<Partslist> partslists = new ArrayList<>();

        partslists.add(new Partslist(
                calculateNumberOfFirstBeams(),
                "Remme i sider, sadles ned i stolper",
                calculateFirstBeamLength(),
                material
        ));

        if (length > maxBeamLength) {
            partslists.add(new Partslist(
                    1, // We'll never need more than 1 extra beam
                    "Remme i sider, sadles ned i stolper (deles)",
                    minBeamLength, // We'll also never need extra beams to be more than 360 cm
                    material
            ));
        }

        return partslists;
    }

    /**
     * Calculates the number of beams needed to build the carport. If the carport is short enough, the calculation will
     * determine that one lumber, cut in two, will be sufficient.
     * @return An integer value of either 1 or 2.
     */
    private int calculateNumberOfFirstBeams() {
        return (length <= maxBeamLength/2) ? 1 : 2;
    }

    /**
     * Determines the length of the first beam.
     * @return An integer value between 360 and 600.
     */
    private int calculateFirstBeamLength() {
        if (length > maxBeamLength) {
            return maxBeamLength;
        }
        else if (length >= minBeamLength) {
            return length;
        }
        else if (length > maxBeamLength/2) {
            return minBeamLength;
        }
        else {
            return length * 2;
        }
    }
}
