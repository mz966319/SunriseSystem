package dbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbTables {
    private static Connection connection;

    public void connectdbTables() {
        try {
            this.connection = dbConnection.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (this.connection == null){
            System.exit(1);
        }
    }

    public ResultSet getLoginTable(String user, String pass, String opt) throws Exception{
        connectdbTables();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM login WHERE username = ? AND password = ? AND division = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2,pass);
            preparedStatement.setString(3,opt);

            resultSet = preparedStatement.executeQuery();  //create a method that returns this result set
            //System.out.println("DatabseTable0: "+resultSet.next());
            return resultSet;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        finally {
            //mSystem.out.println("DatabseTable: "+resultSet.next());
            preparedStatement.close();
            resultSet.close();
        }
    }
}
