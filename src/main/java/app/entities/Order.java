package app.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private int id;
    private int customerId;
    private double total;
    private String status;
    private int widthCm;
    private int lengthCm;
    private String comments;
    private LocalDateTime createdAt;

    // used by controller
    public Order( int customerId, double total, String status, int widthCm, int lengthCm, String comments, LocalDateTime createdAt) {
        this.customerId=customerId;
        this.total=total;
        this.status=status;
        this.widthCm=widthCm;
        this.lengthCm=lengthCm;
        this.comments=comments;
        this.createdAt = createdAt;
    }

    public Order(int id, int customerId, double total, String status, int widthCm, int lengthCm, String comments, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.status = status;
        this.widthCm = widthCm;
        this.lengthCm = lengthCm;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(int widthCm) {
        this.widthCm = widthCm;
    }

    public int getLengthCm() {
        return lengthCm;
    }

    public void setLengthCm(int lengthCm) {
        this.lengthCm = lengthCm;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && customerId == order.customerId && Double.compare(total, order.total) == 0 && widthCm == order.widthCm && lengthCm == order.lengthCm && Objects.equals(status, order.status) && Objects.equals(comments, order.comments) && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, total, status, widthCm, lengthCm, comments, createdAt);
    }
}
