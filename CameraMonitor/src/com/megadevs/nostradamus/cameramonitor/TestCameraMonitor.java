package com.megadevs.nostradamus.cameramonitor;


public class TestCameraMonitor {
	
	public static void main(String[] args) {
		CameraMonitor mon = new CameraMonitor();
		mon.setInterval(10000);
		mon.recoverCams();
		//Debug
		/*
		IPCamera cam1 = new IPCamera("whymca-padiglione2","http://10.1.90.245:8080/shot.jpg");
		mon.addCamera(cam1);
		*/
		//----
		mon.startMonitor();
	}

}
