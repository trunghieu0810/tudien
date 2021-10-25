package com.example.tudien2;

import java.sql.*;

public class JDBCConnection {
    //Tạo kết nối với MySQL
    public static Connection getJDBCConnection() {
        final String url = "jdbc:mysql://localhost:3306/dictionary";
        final String user = "root";
        final String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url,user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
