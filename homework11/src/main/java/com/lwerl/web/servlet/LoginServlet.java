package com.lwerl.web.servlet;

import com.lwerl.web.helper.TemplateHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lWeRl on 04.09.2017.
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Object login = req.getSession().getAttribute("login");
        Map<String, Object> data = new HashMap<>();
        data.put("login", login);
        try {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().print(TemplateHelper.getPage("login", data));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameterMap().get("login") == null) {
            req.getSession().setAttribute("login", null);
        } else {
            req.getSession().setAttribute("login", req.getParameter("login"));
        }

        doGet(req, resp);
    }
}
