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
public class AddUser extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter(UserEntity.username);
		String newLongitude = req.getParameter(UserEntity.longitude);
		String newLatitude = req.getParameter(UserEntity.latitude);
		String newEmail = req.getParameter(UserEntity.email);
		String newMeshComponents = req.getParameter(UserEntity.mesh_components);
		String newGoogleID = req.getParameter(UserEntity.google_id);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Entity user = new Entity(UserEntity.name, newGoogleID);
		user.setProperty(UserEntity.username, username);
		user.setProperty(UserEntity.longitude, newLongitude);
		user.setProperty(UserEntity.latitude, newLatitude);
		user.setProperty(UserEntity.email, newEmail);
		user.setProperty(UserEntity.mesh_components, newMeshComponents);
		user.setProperty(UserEntity.google_id, newGoogleID);
		
		datastore.put(user);
	}
	
}
