package app.entities;

public class Customers {

    private int id;
    private String fullname;
    private String email;
    private String address;
    private int phoneNumber;
    private int userId;
    private int postalCode;

    public Customers(int id, String fullname,  String email, String address, int phoneNumber, int userId, int postalCode) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.postalCode = postalCode;
    }

    public Customers(String fullname,  String email, String address, int phoneNumber, int userId, int postalCode) {
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "postalCode=" + postalCode +
                ", userId=" + userId +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                ", id=" + id +
                '}';
    }
}
