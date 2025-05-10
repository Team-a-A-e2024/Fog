package app.entities;

import java.sql.Date;
import java.util.Objects;

public class User {
    private int id;
    private String Email;
    private String password;
    private String role;
    private Date passwordChangeDate;

    public User(String email, String password, String role, Date passwordChangeDate) {
        Email = email;
        this.password = password;
        this.role = role;
        this.passwordChangeDate = passwordChangeDate;
    }

    public User(int id, String email, String password, String role, Date passwordChangeDate) {
        this.id = id;
        Email = email;
        this.password = password;
        this.role = role;
        this.passwordChangeDate = passwordChangeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(Email, user.Email) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(passwordChangeDate, user.passwordChangeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Email, password, role, passwordChangeDate);
    }
}
