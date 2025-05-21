package app.entities;

import java.util.Objects;

public class Customer {

    private int id;
    private String fullname;
    private String email;
    private String address;
    private String phoneNumber;
    private User salesRep;
    private int postalCode;

    public Customer(int id, String fullname, String email, String address, String phoneNumber, User salesRep, int postalCode) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salesRep = salesRep;
        this.postalCode = postalCode;
    }

    public Customer(String fullname, String email, String address, String phoneNumber, int postalCode) {
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && postalCode == customer.postalCode && Objects.equals(fullname, customer.fullname) && Objects.equals(email, customer.email) && Objects.equals(address, customer.address) && Objects.equals(phoneNumber, customer.phoneNumber) && Objects.equals(salesRep, customer.salesRep);
    }

    @Override
    public String toString() {
        return "Customer{" +
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
