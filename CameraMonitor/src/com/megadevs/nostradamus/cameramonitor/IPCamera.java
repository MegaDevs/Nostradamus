package com.megadevs.nostradamus.cameramonitor;

import java.io.IOException;

public class IPCamera {
	
	private String Id;
	private String host;
	
	public IPCamera(String Id, String host){
		setHost(host);
		setId(Id);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	
	public byte[] getFrame() {
		try {
			return Utils.getBytesFromUrl(host);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
}
