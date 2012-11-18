package com.megadevs.nostradamus.backend;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
public class GetNearbyUsers extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		String latitude = req.getParameter(UserEntity.latitude);
//		String longitude = req.getParameter(UserEntity.longitude);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
//		Filter f = new Query.FilterPredicate(CameraEntity.id, FilterOperator.EQUAL, cameraID);
		Query q = new Query(UserEntity.name);
		PreparedQuery pq = datastore.prepare(q);

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (Entity e : pq.asIterable()) {
			Map<String, Object> properties = e.getProperties();
			list.add(properties);
		}
		
		resp.getWriter().write(Utils.prepareResponse(list, null));
		
	}
}
