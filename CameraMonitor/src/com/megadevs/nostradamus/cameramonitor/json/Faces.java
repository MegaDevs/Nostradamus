
package com.megadevs.nostradamus.cameramonitor.json;

import java.util.List;

public class Faces{
   	private List<Face_detection> face_detection;
   	private List<Scene_understanding> scene_understanding;
   	private String url;
   	private Usage usage;

 	public List<Face_detection> getFace_detection(){
		return this.face_detection;
	}
	public void setFace_detection(List<Face_detection> face_detection){
		this.face_detection = face_detection;
	}
 	public List<Scene_understanding> getScene_understanding(){
		return this.scene_understanding;
	}
	public void setScene_understanding(List<Scene_understanding> scene_understanding){
		this.scene_understanding = scene_understanding;
	}
 	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
 	public Usage getUsage(){
		return this.usage;
	}
	public void setUsage(Usage usage){
		this.usage = usage;
	}
}
