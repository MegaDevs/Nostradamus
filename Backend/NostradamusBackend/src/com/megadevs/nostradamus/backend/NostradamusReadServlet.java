package com.megadevs.nostradamus.backend;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
public class NostradamusReadServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String type = req.getParameter("type");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Filter f = new Query.FilterPredicate("type", FilterOperator.EQUAL, type);
		Query q = new Query("disaster").setFilter(f);
		
		PreparedQuery pq = datastore.prepare(q);
		resp.getWriter().println("Successfully retrived values!");

		System.out.println(pq.countEntities(FetchOptions.Builder.withDefaults()));
		
		for (Entity result : pq.asIterable()) {
			Map<String, Object> obj = result.getProperties();
			for (String s : obj.keySet())
				resp.getWriter().println(s + " = " + obj.get(s));
		}
	}
	
}
