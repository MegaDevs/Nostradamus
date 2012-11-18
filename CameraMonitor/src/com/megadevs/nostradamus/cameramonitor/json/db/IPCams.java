
package com.megadevs.nostradamus.cameramonitor.json.db;


public class IPCams{
   	private String faces;
   	private String hoster;
   	private String id;
   	private String latitude;
   	private String longitude;
   	private String probresume;
   	private String snapshot;
   	private String timestamp;

 	public String getFaces(){
		return this.faces;
	}
	public void setFaces(String faces){
		this.faces = faces;
	}
 	public String getHoster(){
		return this.hoster;
	}
	public void setHoster(String hoster){
		this.hoster = hoster;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
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
 	public String getProbresume(){
		return this.probresume;
	}
	public void setProbresume(String probresume){
		this.probresume = probresume;
	}
 	public String getSnapshot(){
		return this.snapshot;
	}
	public void setSnapshot(String snapshot){
		this.snapshot = snapshot;
	}
 	public String getTimestamp(){
		return this.timestamp;
	}
	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}
}
