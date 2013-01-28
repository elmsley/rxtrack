package com.rxtrack.model;

public class DosageListItem {
	private String dosage = "Unknown Dosage";
	private int inventory = 1000000;
	private int mitte = 1;
	private String sig = "Unknown directions";
	private String bin = "TBD";
	private String pictures = "1E";
	private Conversion conversion;
	private String flags;
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public int getMitte() {
		return mitte;
	}
	public void setMitte(int mitte) {
		this.mitte = mitte;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public String getPictures() {
		return pictures;
	}
	public void setPictures(String pictures) {
		this.pictures = pictures;
	}
	public Conversion getConversion() {
		return conversion;
	}
	public void setConversion(Conversion conversion) {
		this.conversion = conversion;
	}
	public String getFlags() {
		return flags;
	}
	public void setFlags(String flags) {
		this.flags = flags;
	}
	
}
