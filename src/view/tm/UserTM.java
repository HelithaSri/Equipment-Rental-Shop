package view.tm;

public class UserTM {
    private String userName;
    private String name;
    private String address;
    private String dob;
    private String role;

    public UserTM() {
    }

    public UserTM(String userName, String name, String address, String dob, String role) {
        this.setUserName(userName);
        this.setName(name);
        this.setAddress(address);
        this.setDob(dob);
        this.setRole(role);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return "UserTM{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
