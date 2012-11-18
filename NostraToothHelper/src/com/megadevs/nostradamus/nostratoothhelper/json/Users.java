
package com.megadevs.nostradamus.nostratoothhelper.json;

import java.util.List;

public class Users{
   	private String email;
   	private String google_id;
   	private String latitude;
   	private String longitude;
   	private String mesh_components;
   	private String username;

 	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
 	public String getGoogle_id(){
		return this.google_id;
	}
	public void setGoogle_id(String google_id){
		this.google_id = google_id;
	}
 	public String getLatitude(){
		return this.latitude;
	}
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
 	public String getLongitude(){
		return this.longitude;
	}
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
 	public String getMesh_components(){
		return this.mesh_components;
	}
	public void setMesh_components(String mesh_components){
		this.mesh_components = mesh_components;
	}
 	public String getUsername(){
		return this.username;
	}
	public void setUsername(String username){
		this.username = username;
	}
}
