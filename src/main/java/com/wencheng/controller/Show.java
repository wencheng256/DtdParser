package com.wencheng.controller;

import com.wencheng.util.FileParseDtd;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = "/show")
public class Show extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String file = request.getParameter("filename");
		String width = request.getParameter("width");
		String height = request.getParameter("height");

		if(width == null){
			width = "250";
		}
		if(height == null){
			height = "100";
			
		}
		request.setAttribute("width", width);
		request.setAttribute("height", height);

		String dirName = DateFormatUtils.format(new Date(), "yyyyMMdd");

		FileParseDtd f = new FileParseDtd(getServletContext().getRealPath("/file/"+ dirName +"/"+file));
		JSONObject json = f.getBigJson("first_item_256", "起始");
		request.setAttribute("json", json.toString());

		String template = "/WEB-INF/views/show.jsp";
		render(request, response, template);
	}


}
