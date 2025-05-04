package app.entities;

import java.util.Date;
import java.util.Objects;

public class Order {
    int id;
    String createdAt;
    String total;
    String status;
    int customerId;
    int partslist_id;
    int length;
    int width;
    String comment;

    public Order(int id, String createdAt, String total, String status, int customerId, int partslist_id, int length, int width, String comment) {
        this.id = id;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
        this.customerId = customerId;
        this.partslist_id = partslist_id;
        this.length = length;
        this.width = width;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPartslist_id() {
        return partslist_id;
    }

    public void setPartslist_id(int partslist_id) {
        this.partslist_id = partslist_id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && customerId == order.customerId && partslist_id == order.partslist_id && length == order.length && width == order.width && Objects.equals(createdAt, order.createdAt) && Objects.equals(total, order.total) && Objects.equals(status, order.status) && Objects.equals(comment, order.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, total, status, customerId, partslist_id, length, width, comment);
    }
}
