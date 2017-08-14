import dao.GenericDAO;
import executor.Executor;
import model.UserDataSet;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;
import static java.sql.DriverManager.registerDriver;

/**
 * Created by lWeRl on 06.08.2017.
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        Driver driver = new org.postgresql.Driver();
        registerDriver(driver);
        Connection connection = getConnection("jdbc:postgresql://localhost:5432/otus", "test", "test");
        connection.setAutoCommit(true);
        Executor executor = new Executor(connection);
        GenericDAO dao = new GenericDAO(executor);
        dao.save(new UserDataSet("test", 1));
        UserDataSet user = dao.load(1, UserDataSet.class);
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getAge());
    }
}
