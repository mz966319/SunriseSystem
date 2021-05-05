package objects;

//CANCELED

public class Driver {
    private String driverID;
    private String name;
    private String phoneNumber;

    public Driver(String driverID, String name, String phoneNumber) {
        this.driverID = driverID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
