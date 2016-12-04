package com.wencheng.controller;

import com.wencheng.exception.FileFormatException;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/to_upload")
public class UploadAction extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jo = new JSONObject();
		try {

			jo = transforFile(request);

		} catch (Exception e) {
			jo.put("success", false);
		}

		json(response, jo);
	}


	private JSONObject transforFile(HttpServletRequest request) throws Exception {

		JSONObject jo = new JSONObject();

		DiskFileItemFactory factory = buildItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);

		if(items.size()>0){
            FileItem item = items.get(0);
            if(!item.getName().endsWith(".dtd")){
                throw new FileFormatException();
            }

            InputStream in = item.getInputStream();
            File f = new File(getServletContext().getRealPath("/file/"+new SimpleDateFormat("YYYYMMdd").format(new Date())+"/"));
            if(!f.exists()){
                f.mkdir();
            }

			String targetName = UUID.randomUUID() + FilenameUtils.getExtension(item.getName());
			File target = new File(f.getAbsolutePath() + "/" + targetName);
			FileUtils.copyToFile(in, target);
            in.close();

            jo.put("success", true);
            jo.put("file",targetName);
        }

        return jo;
	}

	private DiskFileItemFactory buildItemFactory() {
		DiskFileItemFactory factory = new DiskFileItemFactory();

		ServletContext servletContext = this.getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		return factory;
	}


}
