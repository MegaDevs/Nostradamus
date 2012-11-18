
package com.megadevs.nostradamus.cameramonitor.json;

import java.util.List;

public class Scene_understanding{
   	private String label;
   	private Number score;

 	public String getLabel(){
		return this.label;
	}
	public void setLabel(String label){
		this.label = label;
	}
 	public Number getScore(){
		return this.score;
	}
	public void setScore(Number score){
		this.score = score;
	}
}
