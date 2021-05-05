package objects;

public class Book {
    private int bookID;
    private String name;
    private double price;
    private String serialNumber;
    private String grade;

    public Book(int bookID, String name, double price, String serialNumber, String grade) {
        this.bookID = bookID;
        this.name = name;
        this.price = price;
        this.serialNumber = serialNumber;
        this.grade = grade;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
