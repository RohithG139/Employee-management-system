package com.employees.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    public static Connection getConnection() {
        try {
            Properties props = new Properties();

            InputStream input = DbConnection.class
                    .getClassLoader()
                    .getResourceAsStream("Application.properties");

            if (input == null) {
                throw new RuntimeException("Application.properties not found");
            }

            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String pass = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }
}
