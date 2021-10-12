package view.tm;

public class CustomerTM {
    private String cusId;
    private String cusName;
    private String cusAddress;
    private String cusNumber;
    private String cusNic;

    public CustomerTM() {
    }

    public CustomerTM(String cusId, String cusName, String cusAddress, String cusNumber, String cusNic) {
        this.setCusId(cusId);
        this.setCusName(cusName);
        this.setCusAddress(cusAddress);
        this.setCusNumber(cusNumber);
        this.setCusNic(cusNic);
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusNumber() {
        return cusNumber;
    }

    public void setCusNumber(String cusNumber) {
        this.cusNumber = cusNumber;
    }

    public String getCusNic() {
        return cusNic;
    }

    public void setCusNic(String cusNic) {
        this.cusNic = cusNic;
    }

    @Override
    public String toString() {
        return "CustomerTM{" +
                "cusId='" + cusId + '\'' +
                ", cusName='" + cusName + '\'' +
                ", cusAddress='" + cusAddress + '\'' +
                ", cusNumber='" + cusNumber + '\'' +
                ", cusNic='" + cusNic + '\'' +
                '}';
    }
}
