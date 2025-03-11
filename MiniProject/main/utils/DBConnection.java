package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12766584";
    private static final String USERNAME = "sql12766584";
    private static final String PASSWORD = "HfCVR4h2FD";;
    private Connection connection = null;
    private Statement statement = null;

    public DBConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            System.out.println("Connection established.");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Query execution failed.");
            e.printStackTrace();
        }
        return resultSet;
    }

    public int executeUpdate(String query) {
        int rowsAffected = 0;
        try {
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Update execution failed.");
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public void close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            statement = null;
            connection = null;
            System.out.println("Connection closed successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to close connection.");
            e.printStackTrace();
        }
    }
}
