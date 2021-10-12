package model;

public class Customer {
    private String custId;
    private String custName;
    private String custAddress;
    private String custNumber;
    private String custNic;

    public Customer() {
    }

    public Customer(String custId, String custName, String custAddress, String custNumber, String custNic) {
        this.setCustId(custId);
        this.setCustName(custName);
        this.setCustAddress(custAddress);
        this.setCustNumber(custNumber);
        this.setCustNic(custNic);
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getCustNic() {
        return custNic;
    }

    public void setCustNic(String custNic) {
        this.custNic = custNic;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId='" + custId + '\'' +
                ", custName='" + custName + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", custNumber='" + custNumber + '\'' +
                ", custNic='" + custNic + '\'' +
                '}';
    }
}
