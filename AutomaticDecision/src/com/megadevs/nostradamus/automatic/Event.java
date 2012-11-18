package com.megadevs.nostradamus.automatic;

import java.util.Hashtable;

import bayesbox.input.BayesBox;
import bayesbox.input.BayesBoxIOExc;
import bayesbox.logic.BayesNet;

public class Event {
	
	public static final int MODE_ENUMERATE=0;
	public static final int MODE_REJECTION=1;
	public static final int MODE_LIKELIHOOD=2;
	public static final int SAMPLES=6000;
	
	private String netFile;
	private String netData;
	private String netURI;
	private BayesNet net;
	public int mode = MODE_LIKELIHOOD;
	
	
	Hashtable<String, Boolean> evidence;
	
	public Event(){}
	
	public Event(String netPath){
		setNetFile(netPath);
	}
	
	public void clearEvidence(){
		evidence.clear();
	}
	
	/**
	 * Is all ready?
	 * @return
	 */
	public boolean isReady(){
		if(net != null && evidence !=null)return true;
		return false;
	}
	
	
	public String getNetFile() {
		return netFile;
	}
	
	
	
	public void setNetFile(String netFile) {
		this.netFile = netFile;
		BayesBox bay = new BayesBox(netFile,false);
		net = bay.getBayesNet();
		evidence = new Hashtable<String, Boolean>();
	}
	
	
	public BayesNet getNet() {
		return net;
	}
	public void setNet(BayesNet net) {
		this.net = net;
	}

	public Hashtable<String, Boolean> getEvidence() {
		return evidence;
	}

	public void setEvidence(Hashtable<String, Boolean> evidence) {
		this.evidence = evidence;
	}
	
	public double ask(String var) throws BayesBoxIOExc{
		switch(mode){
		case MODE_ENUMERATE:
			return getNet().enumerationAsk(var, evidence,BayesNet.MODE_DESCRIPTION)[0];

		case MODE_LIKELIHOOD:
			return getNet().likelihoodWeighting(var, evidence, SAMPLES, BayesNet.MODE_DESCRIPTION)[0];

		case MODE_REJECTION:
			return getNet().rejectionSample(var, evidence, SAMPLES, BayesNet.MODE_DESCRIPTION)[0];

		}
		return -1;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getNetData() {
		return netData;
	}

	public void setNetData(String netData) {
		this.netData = netData;
		BayesBox bay = new BayesBox(netData,true);
		net = bay.getBayesNet();
		evidence = new Hashtable<String, Boolean>();
	}

	public String getNetURI() {
		return netURI;
	}

	public void setNetURI(String netURI) {
		this.netURI = netURI;
	}

}
