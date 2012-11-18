package com.megadevs.nostradamus.automatic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import bayesbox.input.BayesBoxIOExc;

import com.google.gson.Gson;

public class Test {
	public static String query_evidence = "nostradamus-whymca.appspot.com/get_disaster_status";
	public static String query_allcam = "nostradamus-whymca.appspot.com/get_all_cameras";
	
	public static void main(String[] args) throws BayesBoxIOExc, IOException, URISyntaxException {
		
		WebcamEvent we = new WebcamEvent();
		//we.setNetFile("/home/ziby/Desktop/Hackaton/webcam.xml");
		System.out.println("Getting schema...");
		HTTPResult executeHTTPUrl = Utils.executeHTTPUrl("megadevs.com/nostradamus/generic.xml", null,null);
		we.setNetData(executeHTTPUrl.getData());
		
		
		System.out.println("Getting evidence...");
		//TODO get evidence
		HTTPResult ev = Utils.executeHTTPUrl(query_evidence, null,null);
		String datajson = ev.getData();
		
		
		Gson g=new Gson();
		ArrayList<HashMap<String,String>> a =(ArrayList<HashMap<String,String>> ) g.fromJson(datajson, ArrayList.class);
		System.out.println(a.size());
		for(int i=0;i<a.size();i++){
			
			HashMap<String, String> cat = a.get(i);
			String k = ""+cat.keySet().toArray()[0];
			String v = cat.get(k);
//			flooding
//			fire
//			earthquake
//			terrorism
//			tornado
				
			if(k.toLowerCase().equals("flooding")){
				if(v.equals("true"))
					we.setEvidence(Catastrophe.FLOODING, true);
				else if(v.equals("false"))
					we.setEvidence(Catastrophe.FLOODING, false);
				else
					we.removeEvidence(Catastrophe.FLOODING);
			}
			
			else if(k.toLowerCase().equals("fire")){
				if(v.equals("true"))
					we.setEvidence(Catastrophe.FIRE, true);
				else if(v.equals("false"))
					we.setEvidence(Catastrophe.FIRE, false);
				else
					we.removeEvidence(Catastrophe.FIRE);
			}
			else if(k.toLowerCase().equals("earthquake")){
				if(v.equals("true"))
					we.setEvidence(Catastrophe.EARTHQUAKE, true);
				else if(v.equals("false"))
					we.setEvidence(Catastrophe.EARTHQUAKE, false);
				else
					we.removeEvidence(Catastrophe.EARTHQUAKE);
			}
				
			else if(k.toLowerCase().equals("terrorism")){
				if(v.equals("true"))
					we.setEvidence(Catastrophe.TERRORISTIC_ATTACK, true);
				else if(v.equals("false"))
					we.setEvidence(Catastrophe.TERRORISTIC_ATTACK, false);
				else
					we.removeEvidence(Catastrophe.TERRORISTIC_ATTACK);
			}
			
			else if(k.toLowerCase().equals("tornado")){
				if(v.equals("true"))
					we.setEvidence(Catastrophe.TORNADO, true);
				else if(v.equals("false"))
					we.setEvidence(Catastrophe.TORNADO, false);
				else
					we.removeEvidence(Catastrophe.TORNADO);
			}
				
		}
		
		HTTPResult ec = Utils.executeHTTPUrl(query_allcam, null,null);
		datajson = ec.getData();
		IPCams[] ips = g.fromJson(datajson,IPCams[] .class);
		boolean people=false;
		boolean isnull=false;
		for(int i=0;i<ips.length && !people;i++){
			try{
				if(Integer.parseInt(ips[i].getFaces())>0)people=true; 
				
			}
			catch(Exception e){
				isnull=true;
			}
		}
		
		if(people){
			we.setEvidence(Entity.PEOPLE, true);
		}else{
			if(isnull){
				we.removeEvidence(Entity.PEOPLE);
			}else
			{
				we.setEvidence(Entity.PEOPLE, false);
			}
		}
		
		System.out.println("ok");
		System.out.println("Elaborating..");
		we.setMode(Event.MODE_ENUMERATE);
		
		if(we.isReady()){
			//we.setEvidence(Entity.PEOPLE, true);
			//we.setEvidence(Catastrophe.EARTHQUAKE, true);
			
			
			//TODO ritorno valori a smiotto
			System.out.println("Percentuale di emergenza: "+ we.getEmergencyPercentage());
			System.out.println("Necessità di Protezione Civile: "+ we.getNecessityOf(Entity.CIVIL_DEFENCE));
			System.out.println("Necessità di Pompieri: "+ we.getNecessityOf(Entity.FIREMAN));
			System.out.println("Necessità di Polizia: "+ we.getNecessityOf(Entity.POLICE));
			System.out.println("Necessità di Esercito: "+ we.getNecessityOf(Entity.ARMY));
			System.out.println("Necessità di Croce Rossa: "+ we.getNecessityOf(Entity.RED_CROSS));
			
			//we.clearEvidence();
		}
	}
	

}
