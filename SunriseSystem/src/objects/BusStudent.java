package objects;

public class BusStudent extends Student{

    private int busStudentID;
    private int busNumber;
    private String path;
    private String subscription;

    public BusStudent(int studentID, String name, String fatherPhone, String motherPhone, String grade, int busStudentID, int busNumber, String path, String subscription) {
        super(studentID, name, fatherPhone, motherPhone, grade);
        this.busStudentID = busStudentID;
        this.busNumber = busNumber;
        this.path = path;
        this.subscription = subscription;
    }




//    public BusStudent(int studentID, String name, String fatherPhone, String motherPhone, String grade, int busID, String path, String subscription) {
//        super(studentID, name, fatherPhone, motherPhone, grade);
//        this.busID = busID;
//        this.path = path;
//        this.subscription = subscription;
//    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public int getBusStudentID() {
        return busStudentID;
    }

    public void setBusStudentID(int busStudentID) {
        this.busStudentID = busStudentID;
    }


}
