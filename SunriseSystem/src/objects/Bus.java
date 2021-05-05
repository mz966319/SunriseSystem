package objects;

public class Bus {
    private int busID;
    private int busNumber;
    private String area;
    private String driverName;
    private String driverPhoneNumber;

    public Bus(int busID, int busNumber, String area, String driverName, String driverPhoneNumber) {
        this.busID = busID;
        this.busNumber = busNumber;
        this.area = area;
        this.driverName = driverName;
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public int getBusID() {
        return busID;
    }

    public void setBusID(int busID) {
        this.busID = busID;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busID='" + busID + '\'' +
                ", busNumber='" + busNumber + '\'' +
                ", area='" + area + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverPhoneNumber='" + driverPhoneNumber + '\'' +
                '}';
    }
}
