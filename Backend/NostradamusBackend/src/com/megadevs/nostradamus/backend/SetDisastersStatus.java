package com.megadevs.nostradamus.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class SetDisastersStatus extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		String earthquake = req.getParameter(DisasterEntity.earthquake);
		if (earthquake == null || earthquake.contains("null"))
			earthquake = "undefined";
		String fire = req.getParameter(DisasterEntity.fire);
		if (fire == null || fire.contains("null"))
			fire = "undefined";
		String flooding = req.getParameter(DisasterEntity.flooding);
		if (flooding == null || flooding.contains("null"))
			flooding = "undefined";
		String terrorism = req.getParameter(DisasterEntity.terrorism);
		if (terrorism == null || terrorism.contains("null"))
			terrorism = "undefined";
		String tornado = req.getParameter(DisasterEntity.tornado);
		if (tornado == null || tornado.contains("null"))
			tornado = "undefined";
		
		Entity earthquakeDisaster = new Entity(DisasterEntity.name, DisasterEntity.earthquake);
		earthquakeDisaster.setProperty(DisasterEntity.id, DisasterEntity.earthquake);
		earthquakeDisaster.setProperty(DisasterEntity.enabled, earthquake);
		
		datastore.put(earthquakeDisaster);
		
		Entity fireDisaster = new Entity(DisasterEntity.name, DisasterEntity.fire);
		fireDisaster.setProperty(DisasterEntity.id, DisasterEntity.fire);
		fireDisaster.setProperty(DisasterEntity.enabled, fire);
		
		datastore.put(fireDisaster);
		
		Entity floodingDisaster = new Entity(DisasterEntity.name, DisasterEntity.flooding);
		floodingDisaster.setProperty(DisasterEntity.id, DisasterEntity.flooding);
		floodingDisaster.setProperty(DisasterEntity.enabled, flooding);
		
		datastore.put(floodingDisaster);
		
		Entity terrorismDisaster = new Entity(DisasterEntity.name, DisasterEntity.terrorism);
		terrorismDisaster.setProperty(DisasterEntity.id, DisasterEntity.terrorism);
		terrorismDisaster.setProperty(DisasterEntity.enabled, terrorism);
		
		datastore.put(terrorismDisaster);
		
		Entity tornadoDisaster = new Entity(DisasterEntity.name, DisasterEntity.tornado);
		tornadoDisaster.setProperty(DisasterEntity.id, DisasterEntity.tornado);
		tornadoDisaster.setProperty(DisasterEntity.enabled, tornado);
		
		datastore.put(tornadoDisaster);
	}
	
}
