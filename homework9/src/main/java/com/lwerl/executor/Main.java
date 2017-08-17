package com.lwerl.executor;

import com.lwerl.executor.dao.GenericDAO;
import com.lwerl.executor.executor.Executor;
import com.lwerl.executor.helper.ConnectionHelper;
import com.lwerl.executor.model.UserDataSet;
import com.lwerl.executor.service.DBService;
import com.lwerl.executor.service.DBServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("com.lwerl.Main");

        try (
                Connection connection = ConnectionHelper.createConnection(
                "jdbc:postgresql://localhost:5432/otus",
                "test",
                "test")
        ){

            Executor executor = new Executor(connection);
            GenericDAO dao = new GenericDAO(executor);
            DBService service = new DBServiceImpl(dao);

            service.saveUser(new UserDataSet("test", 1));
            UserDataSet user = service.getUser(7L);

            logger.log(Level.INFO, "User id: {0}", user.getId());
            logger.log(Level.INFO, "User name: {0}", user.getName());
            logger.log(Level.INFO, "User age: {0}", user.getAge());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}
