
package com.megadevs.nostradamus.cameramonitor.json;

import java.util.List;

public class Usage{
   	private String api_id;
   	private String quota;
   	private String status;

 	public String getApi_id(){
		return this.api_id;
	}
	public void setApi_id(String api_id){
		this.api_id = api_id;
	}
 	public String getQuota(){
		return this.quota;
	}
	public void setQuota(String quota){
		this.quota = quota;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
