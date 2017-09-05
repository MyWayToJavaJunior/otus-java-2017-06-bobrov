package com.lwerl.web.servlet;

import com.lwerl.web.helper.TemplateHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by lWeRl on 05.09.2017.
 */
public class InformationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getSession().getAttribute("login") != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().print(TemplateHelper.getPage("information", new HashMap<>()));
            } else {
                resp.sendRedirect("");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
