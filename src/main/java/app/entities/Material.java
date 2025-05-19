package app.entities;

import java.util.Objects;

public class Material {
    private int id;
    private String description;
    private String unit;
    private double price;

    public Material(int id, String description, String unit, double price) {
        this.id = id;
        this.description = description;
        this.unit = unit;
        this.price = price;
    }

    public Material(String description, String unit, double price) {
        this.description = description;
        this.unit = unit;
        this.price = price;
    }

    public Material(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material material)) return false;
        return id == material.id && Double.compare(price, material.price) == 0 && Objects.equals(description, material.description) && Objects.equals(unit, material.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, unit, price);
    }
}
