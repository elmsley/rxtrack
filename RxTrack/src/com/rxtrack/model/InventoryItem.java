package com.rxtrack.model;

public class InventoryItem {
	private String dosage;
	private Integer inventory;
	private Integer mitte;
	private String sig;
	private String bin;
	private String pictures;
	private Conversion conversion;
	private Integer dispensed = 0;

	public Integer getDispensed() {
		return dispensed;
	}

	public void setDispensed(Integer dispensed) {
		this.dispensed = dispensed;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getMitte() {
		return mitte;
	}

	public void setMitte(Integer mitte) {
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

	public InventoryItem(DosageListItem dli) {
		dosage = dli.getDosage();
		inventory = dli.getInventory();
		mitte = dli.getMitte();
		sig = dli.getSig();
		bin = dli.getBin();
		pictures = dli.getPictures();
		conversion = dli.getConversion();
	}
	
	public InventoryItem(){
		// used for going backwards
	}
}
