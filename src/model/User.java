package model;

public class User {
    private String userName;
    private String password;
    private String name;
    private String address;
    private String dob;
    private String role;

    public User() {
    }

    public User(String userName, String password, String name, String address, String dob, String role) {
        this.setUserName(userName);
        this.setPassword(password);
        this.setName(name);
        this.setAddress(address);
        this.setDob(dob);
        this.setRole(role);
    }

    public User(String userName, String password) {
        this.setUserName(userName);
        this.setPassword(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
