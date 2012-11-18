package com.megadevs.nostradamus.automatic;

import java.io.IOException;
import java.net.URISyntaxException;

import bayesbox.input.BayesBoxIOExc;

public class Test {
	
	public static void main(String[] args) throws BayesBoxIOExc, IOException, URISyntaxException {
		
		WebcamEvent we = new WebcamEvent();
		//we.setNetFile("/home/ziby/Desktop/Hackaton/webcam.xml");
		System.out.println("Getting schema..");
		HTTPResult executeHTTPUrl = Utils.executeHTTPUrl("megadevs.com/nostradamus/generic.xml", null,null);
		we.setNetData(executeHTTPUrl.getData());
		System.out.println("Elaborating..");
		we.setMode(Event.MODE_ENUMERATE);
		
		if(we.isReady()){
			//we.setEvidence(Entity.PEOPLE, true);
			//we.setEvidence(Catastrophe.EARTHQUAKE, true);
			System.out.println("Percentuale di emergenza: "+ we.getEmergencyPercentage());
			System.out.println("Necessità di Protezione Civile: "+ we.getNecessityOf(Entity.CIVIL_DEFENCE));
			System.out.println("Necessità di Pompieri: "+ we.getNecessityOf(Entity.FIREMAN));
			System.out.println("Necessità di Polizia: "+ we.getNecessityOf(Entity.POLICE));
			System.out.println("Necessità di Esercito: "+ we.getNecessityOf(Entity.ARMY));
			System.out.println("Necessità di Croce Rossa: "+ we.getNecessityOf(Entity.RED_CROSS));
			
			we.clearEvidence();
		}
	}

}
