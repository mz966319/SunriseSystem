package objects;

public class Student {
    private int studentID;
    private String name;
    private String fatherPhone;
    private String motherPhone;
    private String grade;

    //constructor
    public Student(int studentID, String name, String fatherPhone, String motherPhone, String grade) {
        this.studentID = studentID;
        this.name = name;
        this.fatherPhone = fatherPhone;
        this.motherPhone = motherPhone;
        this.grade = grade;
    }

    //setters and getters

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherPhone() {
        return fatherPhone;
    }

    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }

    public String getMotherPhone() {
        return motherPhone;
    }

    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


}


