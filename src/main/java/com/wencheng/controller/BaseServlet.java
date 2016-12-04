package com.wencheng.controller;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BaseServlet extends HttpServlet {


    void render(HttpServletRequest request, HttpServletResponse response, String template) throws ServletException, IOException {
        request.getRequestDispatcher(template).forward(request, response);
    }

    void json(HttpServletResponse response, JSONObject jo) throws IOException {
        response.setHeader("ContentType", "application-json");
        response.getWriter().println(jo);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}
