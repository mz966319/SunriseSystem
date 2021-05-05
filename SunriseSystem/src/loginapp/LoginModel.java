package loginapp;

import java.sql.*;

import dbUtil.dbConnection;
public class LoginModel {
    private static Connection connection;

    public LoginModel(){
        try {
            this.connection = dbConnection.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (this.connection == null){
            System.exit(1);
        }
    }

    public boolean isDatabaseConnected(){
        return this.connection != null;
    }


    //return true if user entered info in the database
    public boolean isLogin(String user, String pass, String opt) throws Exception {
        createTables();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM login WHERE Username = ? AND Password = ? AND Type = ?";

        try {
            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2,pass);
            preparedStatement.setString(3,opt);

            resultSet = preparedStatement.executeQuery();  //create a method that returns this result set

            //System.out.println(resultSet.next());
            if (resultSet.next()){
                return true;
            }
            return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        finally {
            preparedStatement.close();
            resultSet.close();
        }
    }
    //create tables if they do not exist
    public void createTables() throws SQLException {
        createStudentsTable();
        createBussesTable();
        createStudentHasBusTable();
//        createDriversTable();
        createLoginTable();
    }

    //check and create students table in the database
    private void createStudentsTable() throws SQLException {
        String sql = "CREATE TABLE students (" +
                "StudentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "StudentName VARCHAR(255) NOT NULL," +
                "Grade VARCHAR(50) NOT NULL," +
                "FatherNumber VARCHAR(50) NOT NULL," +
                "MotherNumber VARCHAR(50) NOT NULL);";

        Statement studentsStatement = connection.createStatement();
        ResultSet studentsResultSet = studentsStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='students'");
        if(!studentsResultSet.next()){
            System.out.println("Building the Students table");
            Statement createStudentsTableStatement=connection.createStatement();
            createStudentsTableStatement.execute(sql);
            createStudentsTableStatement.close();
        }
        studentsStatement.close();
        studentsResultSet.close();
    }

    //check and create busses table in the database
    private void createBussesTable() throws SQLException {
        String sql = "CREATE TABLE busses(" +
                "BusID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DriverName VARCHAR(100) NOT NULL," +
                "DriverPhoneNumber VARCHAR(50) NOT NULL," +
                "BusNumber INTEGER NOT NULL," +
                "Area TEXT NOT NULL);";

        Statement bussesStatement = connection.createStatement();
        ResultSet bussesResultSet = bussesStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='busses'");
        if (!bussesResultSet.next()) {
            System.out.println("Building the Busses table");
            Statement createBussesTableStatement = connection.createStatement();
            createBussesTableStatement.execute(sql);
            createBussesTableStatement.close();
        }
        bussesStatement.close();
        bussesResultSet.close();
    }


    //check and create Students Has Bus table in the database
    private void createStudentHasBusTable() throws SQLException {

        String sql = "CREATE TABLE studentshasbus (" +
                "BusStudentID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "StudentID INTEGER NOT NULL," +
                "BusID INTEGER NOT NULL," +
                "Path VARCHAR(100)," +
                "Subscription VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (StudentID) REFERENCES students(StudentID)," +
                "FOREIGN KEY (BusID) REFERENCES busses(BusID));";

        Statement studentsHasBusStatement = connection.createStatement();
        ResultSet studentsHasBusResultSet = studentsHasBusStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='studentshasbus'");
        if(!studentsHasBusResultSet.next()){
            System.out.println("Building the students Has Bus table");
            Statement createStudentsHasBusTableStatement=connection.createStatement();
            createStudentsHasBusTableStatement.execute(sql);
            createStudentsHasBusTableStatement.close();
        }
        studentsHasBusStatement.close();
        studentsHasBusResultSet.close();
    }

//    //check and create drivers table in the database
//    private void createDriversTable() throws SQLException {
//        Statement driversStatement = connection.createStatement();
//        ResultSet driversResultSet = driversStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='drivers'");
//        if(!driversResultSet.next()){
//            System.out.println("Building the drivers table");
//            Statement createDriversTableStatement=connection.createStatement();
//            createDriversTableStatement.execute("CREATE TABLE drivers(DriverID int IDENTITY(1,1)  NOT NULL,Name varchar(100) NOT NULL,PhoneNumber varchar(50) NOT NULL,BusID int NOT NULL,PRIMARY KEY (DriverID),FOREIGN KEY (BusID) REFERENCES busses(BusID));");
//            createDriversTableStatement.close();
//        }
//        driversStatement.close();
//        driversResultSet.close();
//    }

    //check and create login table in the database
    private void createLoginTable() throws SQLException {
        String sql = "CREATE TABLE login(" +
                "LoginID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username VARCHAR(255) NOT NULL," +
                "Password VARCHAR(255) NOT NULL," +
                "Type VARCHAR(255) NOT NULL);";

        Statement loginStatement = connection.createStatement();
        ResultSet loginResultSet = loginStatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='login'");
        if(!loginResultSet.next()){
            System.out.println("Building the login table");
            Statement createLoginTableStatement=connection.createStatement();
            createLoginTableStatement.execute(sql);
            createLoginTableStatement.close();
        }
        loginStatement.close();
        loginResultSet.close();
        //addToLoginTable("m","m","User");
    }



    //add new login user for the app
    private void addToLoginTable(String username, String password, String type) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO login values(?,?,?,?)");
        prep.setString(2,username);
        prep.setString(3,password);
        prep.setString(4,type);
        prep.execute();
        prep.close();

    }
}
