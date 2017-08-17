package com.lwerl.executor.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Executor {

    private static Logger logger = Logger.getLogger("com.lwerl.executor.Executor");
    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String query) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQLError: " + query, e);
            return -1;
        }
    }

    public <T> T execQuery(String query, ResultMapper<T> mapper) {
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            return mapper.map(resultSet);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQLError: " + query, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}
