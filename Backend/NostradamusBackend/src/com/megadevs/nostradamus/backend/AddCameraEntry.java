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
public class AddCameraEntry extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String newLocation = req.getParameter(CameraEntity.id);
		String newHost = req.getParameter(CameraEntity.hoster);
		String newSnapshot = req.getParameter(CameraEntity.snapshot);
		String newFaces = req.getParameter(CameraEntity.faces);
		String newLatitude = req.getParameter(CameraEntity.latitude);
		String newLongitude = req.getParameter(CameraEntity.longitude);
		String newProbResume = req.getParameter(CameraEntity.probresume);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity camera = new Entity(CameraEntity.name);
		camera.setProperty(CameraEntity.id, newLocation);
		camera.setProperty(CameraEntity.hoster, newHost);
		camera.setProperty(CameraEntity.snapshot, newSnapshot);
		camera.setProperty(CameraEntity.faces, newFaces);
		camera.setProperty(CameraEntity.latitude, newLatitude);
		camera.setProperty(CameraEntity.longitude, newLongitude);
		camera.setProperty(CameraEntity.probresume, newProbResume);
		camera.setProperty(CameraEntity.timestamp, System.currentTimeMillis());
		
		datastore.put(camera);
	}
}
