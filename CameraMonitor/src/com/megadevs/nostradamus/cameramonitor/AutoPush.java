package com.megadevs.nostradamus.cameramonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AutoPush {

	
	public static int intervallo = 2000;
	
	public static void main(String[] args) {
		int index=0;
		while(true){
			index++;
			System.out.println("Getting frame "+index);
			IPCamera cam1 = new IPCamera("whymca-padiglione2","http://10.1.90.245:8080/shot.jpg");
			byte[] frame = cam1.getFrame();
			if(frame==null)System.out.println("frame nullo");

			File f=new File("/home/ziby/Desktop/shoot.jpg");
			FileOutputStream fo;
			try {
				fo = new FileOutputStream(f);
				fo.write(frame);
				fo.close();
				System.out.println("Pushing "+index);
				HTTPPostParameters params = new HTTPPostParameters();
				params.addParam("file", f);
				try {
					HTTPResult executeHTTPUrlPost = Utils.executeHTTPUrlPost("http://megadevs.com/nostradamus/push.php", params, null);
					System.out.println(executeHTTPUrlPost.getData());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Done.");
				try {
					Thread.sleep(intervallo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
