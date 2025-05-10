package app.entities;

import java.util.Objects;

public class Partslist {
    private int id;
    private int quantity;
    private String description;
    private int length;
    private Material material;

    public Partslist(int id, int quantity, String description, int length, Material material) {
        this.id = id;
        this.quantity = quantity;
        this.description = description;
        this.length = length;
        this.material = material;
    }

    public Partslist(int quantity, String description, int length, Material material) {
        this.quantity = quantity;
        this.description = description;
        this.length = length;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "Partslist{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", length=" + length +
                ", material=" + material +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partslist partslist)) return false;
        return id == partslist.id && quantity == partslist.quantity && length == partslist.length && Objects.equals(description, partslist.description) && Objects.equals(material, partslist.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, description, length, material);
    }
}
