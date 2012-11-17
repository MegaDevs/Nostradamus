package com.megadevs.nostradamus.automatic;

import bayesbox.input.BayesBoxIOExc;

public class WebcamEvent extends Event {

	public static final  String VAR_EMERGENCY ="Soccorso Necessario";

	public static final String VAR_POLICE= "Polizia";
	public static final String VAR_FIREMAN= "Pompieri";
	public static final  String VAR_ARMY= "Esercito";
	public static final  String VAR_CIVIL= "Protezione Civile";
	public static final  String VAR_REDCROSS= "Croce Rossa";

	public static final  String VAR_EARTHQUAKE= "Terremoto";
	public static final  String VAR_FLOODING= "Alluvione";
	public static final  String VAR_TORNADO= "Tornado";
	public static final  String VAR_TERRORISTIC_ATTACK= "Attacco Terroristico";
	public static final  String VAR_FIRE= "Incendio";
	public static final  String VAR_PEOPLE= "Persone sul posto";
	


	public double getEmergencyPercentage() throws BayesBoxIOExc{
		return ask(VAR_EMERGENCY);
	}

	public void setEvidence(int varId, boolean value){
		
		switch(varId){
		
		case Catastrophe.EARTHQUAKE:
			getEvidence().put(VAR_EARTHQUAKE, value);
			break;
			
		case Catastrophe.FLOODING:
			getEvidence().put(VAR_FLOODING, value);
			break;
			
		case Catastrophe.TORNADO:
			getEvidence().put(VAR_TORNADO, value);
			break;
			
		case Catastrophe.TERRORISTIC_ATTACK:
			getEvidence().put(VAR_TERRORISTIC_ATTACK, value);
			break;

		case Catastrophe.FIRE:
			getEvidence().put(VAR_FIRE, value);
			break;

		case Entity.PEOPLE:
			getEvidence().put(VAR_PEOPLE, value);
			break;			
		}

	}

	public void removeEvidence(int varId){
		switch(varId){
		
		case Catastrophe.EARTHQUAKE:
			getEvidence().remove(VAR_EARTHQUAKE);
			break;
			
		case Catastrophe.FLOODING:
			getEvidence().remove(VAR_FLOODING);
			break;
			
		case Catastrophe.TORNADO:
			getEvidence().remove(VAR_TORNADO);
			break;
			
		case Catastrophe.TERRORISTIC_ATTACK:
			getEvidence().remove(VAR_TERRORISTIC_ATTACK);
			break;

		case Catastrophe.FIRE:
			getEvidence().remove(VAR_FIRE);
			break;

		case Entity.PEOPLE:
			getEvidence().remove(VAR_PEOPLE);
			break;
			
		}
	}


	public double getNecessityOf(int entityId) throws BayesBoxIOExc{
		double ris=0;

		switch(entityId){
		case Entity.FIREMAN:
			ris = ask(VAR_FIREMAN);
			break;

		case Entity.POLICE:
			ris = ask(VAR_POLICE);				
			break;

		case Entity.ARMY:
			ris = ask(VAR_ARMY);
			break;

		case Entity.CIVIL_DEFENCE:
			ris = ask(VAR_CIVIL);
			break;

		case Entity.RED_CROSS:
			ris = ask(VAR_REDCROSS);
			break;
		}

		return ris;
	}



}
