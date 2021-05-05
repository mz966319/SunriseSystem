package user;

import java.sql.*;
import java.util.ArrayList;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Bus;
import objects.BusStudent;

public class UserHomeModel {
    private static Connection connection;

    public UserHomeModel() {
        try {
            this.connection = dbConnection.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (this.connection == null){
            System.exit(1);
        }
    }

    //add passed data to the student table
    public void addToStudentTable(String name, String fatherPhone, String motherPhone,String grade) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO students values(?,?,?,?,?)");
        prep.setString(2,name);
        prep.setString(3,grade);
        prep.setString(4,fatherPhone);
        prep.setString(5,motherPhone);
        prep.execute();
        prep.close();

        //return getStudentID(name,fatherPhone,motherPhone,grade);




    }

    //add to student has bus table and to student table(if needed)
    public void addToStudentHasBusTable(String name, String fatherPhone, String motherPhone,String grade,
                                        int busNumber,String path, String subscription) throws SQLException{

        if(getStudentID(name,fatherPhone,motherPhone,grade)<0) { //check if student already exist in students table
            addToStudentTable(name, fatherPhone, motherPhone, grade);
        }

        int studentID = getStudentID(name,fatherPhone,motherPhone,grade);
        int busID = getBusIDWithBusNumber(busNumber);
        if (getBusStudentID(studentID)<0) {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO studentshasbus values(?,?,?,?,?)");
            prep.setInt(2, studentID);
            prep.setInt(3, busID);
            prep.setString(4, path);
            prep.setString(5, subscription);
            prep.execute();
            prep.close();
        }






    }

    //add to busses table
    public void addToBussesTable(String name, String phoneNumber, String busNumber, String area) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO busses values(?,?,?,?,?)");
        prep.setString(2,name);
        prep.setString(3,phoneNumber);
        prep.setString(4,busNumber);
        prep.setString(5,area);
        prep.execute();
        prep.close();

    }

    //returns the bus id using the bus number
    public int getBusIDWithBusNumber(int number) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT BusID FROM busses WHERE BusNumber = ?";

        try {
            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1,number);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
            return -1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -2;
        }

        finally {
            preparedStatement.close();
            resultSet.close();
        }

    }

    //returns the bus number using the bus ID
    public int getBusNumberWithBusID(int busID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT BusNumber FROM busses WHERE BusID = ?";

        try {
            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1,busID);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
            return -1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -2;
        }

        finally {
            preparedStatement.close();
            resultSet.close();
        }

    }

    //return bus student id from studenthasbus table
    public int getBusStudentID(int studentID) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT BusStudentID FROM studentshasbus WHERE StudentID = ?";

        try {
            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1,studentID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
            return -1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -2;
        }

        finally {
            preparedStatement.close();
            resultSet.close();
        }

    }

    //return student ID from student table
    public int getStudentID(String name, String fatherPhone, String motherPhone,String grade) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT StudentID FROM students WHERE StudentName = ? AND Grade = ? AND FatherNumber = ? AND MotherNumber = ?";

        try {
            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,grade);
            preparedStatement.setString(3,fatherPhone);
            preparedStatement.setString(4,motherPhone);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
            return -1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -2;
        }

        finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    //return a list of all students in database
    public ObservableList<BusStudent> getAllStudents(){
        ObservableList<BusStudent> students = FXCollections.observableArrayList();


        String sql = "SELECT * FROM students";

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);


            while (resultSet.next()){
                int studentID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String grade = resultSet.getString(3);
                String fatherPhone = resultSet.getString(4);
                String motherPhone = resultSet.getString(5);

                //System.out.print(studentID+" "+name+" "+grade+" "+fatherPhone+" "+motherPhone+" ");

                PreparedStatement preparedStatement = null;
                ResultSet studentResultSet = null;
                String studentSQL = "SELECT BusStudentID, BusID, Path, Subscription FROM studentshasbus WHERE StudentID = ?";

                try {
                    preparedStatement = this.connection.prepareStatement(studentSQL);
                    preparedStatement.setInt(1,studentID);


                    studentResultSet = preparedStatement.executeQuery();  //create a method that returns this result set

                    if (studentResultSet.next()){
                        int busStudentID = studentResultSet.getInt("BusStudentID");
                        int busID = studentResultSet.getInt("BusID"); //
                        int busNumber = getBusNumberWithBusID(busID);
                        String path = studentResultSet.getString("Path");
                        String subscription = studentResultSet.getString("Subscription");


                        students.add(new BusStudent(studentID,name,fatherPhone,motherPhone,grade,busStudentID,busNumber,path,subscription));

                    }
                    preparedStatement.close();
                    studentResultSet.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }

            resultSet.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return students;
    }

    //return a list of all students in database
    public ObservableList<Bus> getAllDrivers(){
        ObservableList<Bus> drivers = FXCollections.observableArrayList();


        String sql = "SELECT * FROM busses";

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);


            while (resultSet.next()){
                int busID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String phone = resultSet.getString(3);
                int busNumber = resultSet.getInt(4);
                String area = resultSet.getString(5);
                    //int busID, String busNumber, String area, String driverName, String driverPhoneNumber) {

                    drivers.add(new Bus(busID,busNumber,area,name,phone));


                //System.out.print(studentID+" "+name+" "+grade+" "+fatherPhone+" "+motherPhone+" ");



            }

            resultSet.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return drivers;
    }

    //return a list of available bus numbers
    public ArrayList<Integer> getBusNumbers(){

        ArrayList<Integer> busNumbers = new ArrayList<Integer>();
        String sql = "SELECT BusNumber FROM busses ORDER BY BusNumber ASC";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                int number = resultSet.getInt(1);

                System.out.println(number);
                busNumbers.add(number);

            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return busNumbers;

    }


    //remove from database methods:

    //remove a student from the student has bus database
    public void removeStudentHasBus(int studentID) throws SQLException {
        String sql = "DELETE FROM studentshasbus WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setInt(1, studentID);

        prep.execute();
        prep.close();

    }

    //remove a student from the student has bus database
    public void removeDriver(int busID) throws SQLException {
        String sql = "DELETE FROM busses WHERE BusID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setInt(1, busID);

        prep.execute();
        prep.close();

    }




    //update the database

    //update students

    //update name in student table
    public void updateStudentName(int studentID,String newName) throws SQLException {
        String sql = "UPDATE students SET StudentName = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newName);
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }

    //update grade in student table
    public void updateStudentGrade(int studentID,String newGrade) throws SQLException {
        String sql = "UPDATE students SET Grade = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newGrade);
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }
    //update father's phone number in student table
    public void updateStudentFatherPhone(int studentID,String newFatherPhone) throws SQLException {
        String sql = "UPDATE students SET FatherNumber = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newFatherPhone);
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }
    //update mother's phone number in student table
    public void updateStudentMotherPhone(int studentID,String newMotherPhone) throws SQLException {
        String sql = "UPDATE students SET MotherNumber = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newMotherPhone);
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }

    //update bus number in studentshasbus table
    public void updateStudentBusNumber(int studentID,String newBusNumber) throws SQLException {
        String sql = "UPDATE studentshasbus SET BusID = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setInt(1, getBusIDWithBusNumber(Integer.parseInt(newBusNumber)));
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }

    //update path in studentshasbus table
    public void updateStudentPath(int studentID,String newPath) throws SQLException {
        String sql = "UPDATE studentshasbus SET Path = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newPath);
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }
    //update subscription in studentshasbus table
    public void updateStudentSubscription(int studentID,String newSubscription) throws SQLException {
        String sql = "UPDATE studentshasbus SET Subscription = ? WHERE StudentID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newSubscription);
        prep.setInt(2,studentID);
        prep.execute();
        prep.close();

    }


    //update drivers

    //update driverName in busses table
    public void updateBusDriverName(int busID,String newName) throws SQLException {
        String sql = "UPDATE busses SET DriverName = ? WHERE BusID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newName);
        prep.setInt(2,busID);
        prep.execute();
        prep.close();
    }

    //update driverName in busses table
    public void updateBusDriverPhoneNumber(int busID,String newPhone) throws SQLException {
        String sql = "UPDATE busses SET DriverPhoneNumber = ? WHERE BusID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newPhone);
        prep.setInt(2,busID);
        prep.execute();
        prep.close();
    }

    //update bus number in busses table
    public void updateBusNumber(int busID,String newBusNumber) throws SQLException {
        String sql = "UPDATE busses SET BusNumber = ? WHERE BusID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newBusNumber);
        prep.setInt(2,busID);
        prep.execute();
        prep.close();
    }

    //update driverName in busses table
    public void updateBusArea(int busID,String newArea) throws SQLException {
        String sql = "UPDATE busses SET Area = ? WHERE BusID = ?";

        PreparedStatement prep = connection.prepareStatement(sql);
        prep.setString(1, newArea);
        prep.setInt(2,busID);
        prep.execute();
        prep.close();
    }







}
