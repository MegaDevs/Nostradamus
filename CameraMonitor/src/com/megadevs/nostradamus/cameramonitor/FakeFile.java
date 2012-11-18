package com.megadevs.nostradamus.cameramonitor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FakeFile {
	private String filename;
	private byte[] data;
	
	public FakeFile(String filename, byte[]dat){
		this.filename=filename;
		data=dat;
	}
	
    public InputStream getStream(){
		InputStream is = new ByteArrayInputStream(data);
		return is;
	}



	public String getFilename() {
		return filename;
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}



	public byte[] getData() {
		return data;
	}



	public void setData(byte[] data) {
		this.data = data;
	}

}
