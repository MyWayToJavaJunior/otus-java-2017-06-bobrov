package com.lwerl.web.servlet;

import com.google.gson.Gson;
import com.lwerl.cache.CacheStatistic;
import com.lwerl.orm.service.DBServiceImpl;
import com.lwerl.web.response.CacheStatisticResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lWeRl on 04.09.2017.
 */
public class StatisticServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        CacheStatistic statistic = (CacheStatistic) DBServiceImpl.getInstance().getCache();
        CacheStatisticResponse statisticResponse = CacheStatisticResponse.getStatistic(statistic);
        String response = gson.toJson(statisticResponse);

        try {
            resp.getWriter().print(response);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
