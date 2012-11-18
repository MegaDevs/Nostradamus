package com.megadevs.nostradamus.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class GetAllCameras extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String callbackMethod = req.getParameter(Utils.CALLBACK);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query(CameraEntity.name);

		PreparedQuery pq = datastore.prepare(q);
		ArrayList<String> insertedKeys = new ArrayList<String>();
		ArrayList<HashMap<String,String>> list= new ArrayList<HashMap<String,String>>();
		
		for (Entity result : pq.asIterable()) {
			if (!insertedKeys.contains(result.getProperty(CameraEntity.id))) {
				
				HashMap<String,String> rsp = new HashMap<String,String>();
				
				for (String s : CameraEntity.getKeys())
					rsp.put(s, String.valueOf(result.getProperty(s)));
				
				list.add(rsp);
				insertedKeys.add((String) result.getProperty(CameraEntity.id));
			}
		}

		resp.setContentType("application/javascript");
		resp.getWriter().println(Utils.prepareResponse(list, callbackMethod));
	}

}
