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
public class GetDisastersStatus extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String callbackMethod = req.getParameter(Utils.CALLBACK);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(DisasterEntity.name);
		
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		
		for (Entity s : pq.asIterable()) {
			HashMap<String, String> current = new HashMap<String, String>();
			String id = (String) s.getProperty(DisasterEntity.id);
			String enabled = (String) s.getProperty(DisasterEntity.enabled);
			current.put(id, enabled);
			
			list.add(current);
		}
		
		resp.setContentType("application/javascript");
		resp.getWriter().println(Utils.prepareResponse(list, callbackMethod));
	}
	
}
