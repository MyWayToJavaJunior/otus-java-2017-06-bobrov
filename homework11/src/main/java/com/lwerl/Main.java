package com.lwerl;

import com.lwerl.orm.model.AddressDataSet;
import com.lwerl.orm.model.PhoneDataSet;
import com.lwerl.orm.model.UserDataSet;
import com.lwerl.orm.service.DBService;
import com.lwerl.orm.service.DBServiceImpl;
import com.lwerl.web.servlet.ErrorServlet;
import com.lwerl.web.servlet.InformationServlet;
import com.lwerl.web.servlet.LoginServlet;
import com.lwerl.web.servlet.StatisticServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Created by lWeRl on 03.09.2017.
 */

public class Main {
    private final static int PORT = 8090;

    public static void main(String[] args) throws Exception {

        Server server = new Server(PORT);

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(400, 599, "/error");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(ErrorServlet.class, "/error");
        context.addServlet(LoginServlet.class, "");
        context.addServlet(StatisticServlet.class, "/stat");
        context.addServlet(InformationServlet.class, "/info");
        context.setErrorHandler(errorHandler);

        server.setHandler(context);

        server.start();

        //fillData();
        makeAction();

        server.join();
    }

    private static void fillData() {
        DBService service = DBServiceImpl.getInstance();

        for (int i = 0; i < 100; i++) {
            UserDataSet user = new UserDataSet("Nick", 28);

            PhoneDataSet phone1 = new PhoneDataSet();
            phone1.setPhone("123456789");
            phone1.setUser(user);

            PhoneDataSet phone2 = new PhoneDataSet();
            phone2.setPhone("987654321");
            phone2.setUser(user);

            AddressDataSet address = new AddressDataSet();
            address.setAddress("Mother Russia");
            address.setUser(user);

            List<PhoneDataSet> phones = Arrays.asList(phone1, phone2);

            user.setAddress(address);
            user.setPhones(phones);

            service.saveUser(user);
        }
    }

    private static void makeAction() {
        Thread t = new Thread(() -> {
            DBService service = DBServiceImpl.getInstance();
            Random random = new Random(System.currentTimeMillis());

            while(true) {

                Long id = (long) (random.nextInt(100) + 1);
                service.getUser(id);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}

