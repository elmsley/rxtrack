package com.rxtrack.model;

import org.apache.poi.util.StringUtil;

public class Patient {
	private String id = "";
	private String name = "";
	private String city = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	private String cleanString(String str){
		if (str==null) return "";
		return str.trim();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Patient){
			Patient comparePatient = (Patient)obj;
			if (cleanString(id).equals(cleanString(comparePatient.id)) &&
				cleanString(name).equals(cleanString(comparePatient.name))){
				return true;
			}
		}
		return super.equals(obj);
	}
}
