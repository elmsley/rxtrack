package com.rxtrack.model;


public class Script {
	private Patient patient;
	private InventoryItem inventoryItem;
	
	// overrides
	private String mitte;
	private String sig;
	private String pics;
	private String date;
	private String time;
	private String rx;
	
	public String getRx() {
		return rx;
	}
	public void setRx(String rx) {
		this.rx = rx;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}
	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}
	public String getMitte() {
		return mitte;
	}
	public void setMitte(String mitte) {
		this.mitte = mitte;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getPics() {
		return pics;
	}
	public void setPics(String pics) {
		this.pics = pics;
	}
	
	public boolean isEmpty(String value){
		return (value == null || value.trim().length()==0);
	}
	
	/**
	 * Make it so it doesn't pass blanks to the VBS because it is space dependent.
	 */
	public void niceify(){
		final String dString = "_";
		if (isEmpty(mitte)) mitte = "0";
		if (isEmpty(sig)) sig = dString;
		if (isEmpty(date)) date = dString;
		
		if (patient!=null){
			if (isEmpty(patient.getId())) patient.setId(dString);
			if (isEmpty(patient.getName())) patient.setName(dString);
		}
	}
}
