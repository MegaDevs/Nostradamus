
package com.megadevs.nostradamus.cameramonitor.json;

import java.util.List;

public class Face_detection{
   	private Boundingbox boundingbox;
   	private Eye_left eye_left;
   	private Eye_right eye_right;
   	private Mouth_l mouth_l;
   	private Mouth_r mouth_r;
   	private String name;
   	private Nose nose;

 	public Boundingbox getBoundingbox(){
		return this.boundingbox;
	}
	public void setBoundingbox(Boundingbox boundingbox){
		this.boundingbox = boundingbox;
	}
 	public Eye_left getEye_left(){
		return this.eye_left;
	}
	public void setEye_left(Eye_left eye_left){
		this.eye_left = eye_left;
	}
 	public Eye_right getEye_right(){
		return this.eye_right;
	}
	public void setEye_right(Eye_right eye_right){
		this.eye_right = eye_right;
	}
 	public Mouth_l getMouth_l(){
		return this.mouth_l;
	}
	public void setMouth_l(Mouth_l mouth_l){
		this.mouth_l = mouth_l;
	}
 	public Mouth_r getMouth_r(){
		return this.mouth_r;
	}
	public void setMouth_r(Mouth_r mouth_r){
		this.mouth_r = mouth_r;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public Nose getNose(){
		return this.nose;
	}
	public void setNose(Nose nose){
		this.nose = nose;
	}
}
