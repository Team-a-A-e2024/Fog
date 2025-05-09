package app.entities;

public class Customer {

    private int id;
    private String fullName;
    private String address;
    private int postalCode;
    private String email;
    private String phoneNumber;
    private int userId;

    public Customer(String address, String fullName, String email, String phoneNumber, int userId, int postalCode, int id) {
        this.address = address;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.postalCode = postalCode;
        this.id = id;
    }

    // used by tests, frameworks
    public Customer() {
    }

    // used by controller
    public Customer(String fullName, String address, int postalCode, String email, String phoneNumber) {
        this.fullName = fullName;
        this.postalCode = postalCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "Customers" +
                "postalCode=" + postalCode +
                ", userId=" + userId +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", fullname='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }
}

