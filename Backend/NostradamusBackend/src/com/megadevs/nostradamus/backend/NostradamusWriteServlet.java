package com.megadevs.nostradamus.backend;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class NostradamusWriteServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	
		Double longitude = Double.valueOf(req.getParameter("log"));
		Double latitude = Double.valueOf(req.getParameter("lat"));
		
		String type = req.getParameter("type");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity disaster = new Entity("disaster");
		disaster.setProperty("type", type);
		disaster.setProperty("latitude", latitude);
		disaster.setProperty("longitude", longitude);
		
		datastore.put(disaster);
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Request processed successfully");
		resp.getWriter().println("Disaster type is " + type);
		resp.getWriter().println("Longitude is " + longitude);
		resp.getWriter().println("Latitude is " + latitude);
	}
}
