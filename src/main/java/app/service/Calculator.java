package app.service;

import app.entities.Material;
import app.entities.Partslist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    private int length;
    private int width;
    private Map<ICalculationStrategy, Material> strategies;

    private List<Partslist> partslists;

    public Calculator(int length, int width) {
        this.length = length;
        this.width = width;
        this.strategies = new HashMap<>();
    }

    public void addStrategy(ICalculationStrategy strategy, Material material) {
        strategies.put(strategy, material);
    }

    public List<Partslist> getPartslists() {
        partslists = new ArrayList<>();

        for (Map.Entry<ICalculationStrategy, Material> entry : strategies.entrySet()) {
            partslists.addAll(entry.getKey().calculate(length, width, entry.getValue()));
        }

        return partslists;
    }

    public double getTotalPrice() {
        double totalPrice = 0;

        for (Partslist part : partslists) {
            totalPrice += ((double)part.getLength() * (double)part.getQuantity()) / 100d * part.getMaterial().getPrice();
        }

        return (double) Math.round(totalPrice * 100) / 100;
    }
}
