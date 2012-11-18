package com.megadevs.nostradamus.cameramonitor;


public class TestCameraMonitor {
	
	public static void main(String[] args) {
		IPCamera cam1 = new IPCamera("HFarm","http://10.1.90.245:8080/shot.jpg");
		
		CameraMonitor mon = new CameraMonitor();
		mon.setCacheDir("/home/ziby/Desktop/cache/");
		mon.setInterval(10000);

		mon.addCamera(cam1);
		
		mon.startMonitor();
		
	}

}
