package app.service;

import app.entities.Material;
import app.entities.Partslist;

import java.util.List;

public interface ICalculationStrategy {
    List<Partslist> calculate(int length, int width, Material material) ;
}
