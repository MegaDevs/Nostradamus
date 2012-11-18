package com.megadevs.nostradamus.backend;

import java.io.IOException;
import java.util.HashMap;

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
public class GetCameraInfo extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String callbackMethod = req.getParameter(Utils.CALLBACK);
		String cameraID = req.getParameter(CameraEntity.id);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Filter f = new Query.FilterPredicate(CameraEntity.id, FilterOperator.EQUAL, cameraID);
		Query q = new Query(CameraEntity.name).setFilter(f);
		
		PreparedQuery pq = datastore.prepare(q);

		int counter = 0;
		int size = pq.countEntities(FetchOptions.Builder.withDefaults());
		
		for (Entity result : pq.asIterable()) {
			if (counter == size - 1) {
				HashMap<String,String> rsp = new HashMap<String,String>();
				
				for (String s : CameraEntity.getKeys())
					rsp.put(s, (String) result.getProperty(s));
				
				resp.setContentType("application/javascript");
				resp.getWriter().println(Utils.prepareResponse(rsp, callbackMethod));
			}
			counter++;
		}
	}
	
}
