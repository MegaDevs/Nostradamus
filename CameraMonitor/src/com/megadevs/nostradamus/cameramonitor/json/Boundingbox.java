
package com.megadevs.nostradamus.cameramonitor.json;

import java.util.List;

public class Boundingbox{
   	private Size size;
   	private Tl tl;

 	public Size getSize(){
		return this.size;
	}
	public void setSize(Size size){
		this.size = size;
	}
 	public Tl getTl(){
		return this.tl;
	}
	public void setTl(Tl tl){
		this.tl = tl;
	}
}
