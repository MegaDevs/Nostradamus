package com.megadevs.nostradamus.backend;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bayesbox.input.BayesBoxIOExc;

import com.megadevs.nostradamus.automatic.Entity;
import com.megadevs.nostradamus.automatic.Event;
import com.megadevs.nostradamus.automatic.HTTPResult;
import com.megadevs.nostradamus.automatic.Utils;
import com.megadevs.nostradamus.automatic.WebcamEvent;

@SuppressWarnings("serial")
public class Bayes extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			WebcamEvent we = new WebcamEvent();
			//we.setNetFile("/home/ziby/Desktop/Hackaton/webcam.xml");
			resp.getWriter().println("Getting schema..");
			HTTPResult executeHTTPUrl = Utils.executeHTTPUrl("megadevs.com/nostradamus/generic.xml", null,null);
			we.setNetData(executeHTTPUrl.getData());
			resp.getWriter().println("Elaborating..");
			we.setMode(Event.MODE_ENUMERATE);

			if(we.isReady()){
				//we.setEvidence(Entity.PEOPLE, true);
				//we.setEvidence(Catastrophe.EARTHQUAKE, true);
				resp.getWriter().println("Percentuale di emergenza: "+ we.getEmergencyPercentage());
				resp.getWriter().println("Necessità di Protezione Civile: "+ we.getNecessityOf(Entity.CIVIL_DEFENCE));
				resp.getWriter().println("Necessità di Pompieri: "+ we.getNecessityOf(Entity.FIREMAN));
				resp.getWriter().println("Necessità di Polizia: "+ we.getNecessityOf(Entity.POLICE));
				resp.getWriter().println("Necessità di Esercito: "+ we.getNecessityOf(Entity.ARMY));
				resp.getWriter().println("Necessità di Croce Rossa: "+ we.getNecessityOf(Entity.RED_CROSS));

				we.clearEvidence();
			}
		} catch (BayesBoxIOExc e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


