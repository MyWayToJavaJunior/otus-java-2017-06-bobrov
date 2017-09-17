package com.lwerl.web.servlet;

import com.google.gson.Gson;
import com.lwerl.cache.CacheStatistic;
import com.lwerl.orm.service.DBService;
import com.lwerl.orm.service.DBServiceImpl;
import com.lwerl.web.response.CacheStatisticResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * Created by lWeRl on 04.09.2017.
 */
public class StatisticServlet extends HttpServlet {


    @Autowired
    private DBService dbService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if (req.getSession().getAttribute("login") != null) {
                Gson gson = new Gson();
                CacheStatistic statistic = dbService.getCache().getStatistic();
                CacheStatisticResponse statisticResponse = CacheStatisticResponse.getStatistic(statistic);
                String response = gson.toJson(statisticResponse);

                resp.getWriter().print(response);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initAutowiredBeans();
        makeAction();
    }

    private void initAutowiredBeans() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private void makeAction() {
        Thread t = new Thread(() -> {
            DBService service = dbService;
            Random random = new Random(System.currentTimeMillis());

            while (true) {

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
