package com.megadevs.nostradamus.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megadevs.nostradamus.cameramonitor.CameraMonitor;

@SuppressWarnings("serial")
public class StopCameraService extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CameraMonitor.STOP = true;
	}
	
}
