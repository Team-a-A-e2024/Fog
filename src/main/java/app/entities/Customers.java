package app.entities;

import java.util.Objects;

public class Customers {

    private int id;
    private String fullname;
    private String email;
    private String address;
    private int phoneNumber;
    private User salesRep;
    private int postalCode;

    public Customers(int id, String fullname,  String email, String address, int phoneNumber, User salesRep, int postalCode) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salesRep = salesRep;
        this.postalCode = postalCode;
    }

    public Customers(String fullname,  String email, String address, int phoneNumber,   User salesRep, int postalCode) {
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salesRep = salesRep;
        this.postalCode = postalCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(User salesRep) {
        this.salesRep = salesRep;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customers customers = (Customers) o;
        return id == customers.id && phoneNumber == customers.phoneNumber && postalCode == customers.postalCode && Objects.equals(fullname, customers.fullname) && Objects.equals(email, customers.email) && Objects.equals(address, customers.address) && Objects.equals(salesRep, customers.salesRep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, email, address, phoneNumber, salesRep, postalCode);
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", salesRep=" + salesRep +
                ", postalCode=" + postalCode +
                '}';
    }
}
