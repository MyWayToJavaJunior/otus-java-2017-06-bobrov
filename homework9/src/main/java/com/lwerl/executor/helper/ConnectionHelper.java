package com.lwerl.executor.helper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionHelper {

    private ConnectionHelper() {
    }

    public static Connection createConnection(String url, String name, String password) throws SQLException {
        Connection result;
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);
        result = DriverManager.getConnection(url, name, password);
        result.setAutoCommit(true);
        return result;
    }
}
