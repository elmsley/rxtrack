package com.rxtrack.model;

import java.util.ArrayList;
import java.util.List;

public class PatientModelProvider {
	private static PatientModelProvider content;
	private List<Patient> patientItems;

	private PatientModelProvider() {
		refreshItems();
	}

	private void refreshItems(){
		patientItems = new ArrayList<Patient>();
		List<Patient> patientList = MasterModel.getInstance().getPatientList();
		for (Patient p : patientList){
			patientItems.add(p);
		}
	}
	
	public static synchronized PatientModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new PatientModelProvider();
		return content;
	}

	public List<Patient> getPatientItems() {
		refreshItems();
		return patientItems;
	}

	public Patient findById(String patientRef) {
		refreshItems();
		if (patientItems!=null){
			for (Patient p : patientItems){
				if (p.getId()!=null && p.getId().equals(patientRef)){
					return p;
				}
			}
		}
		return null;
	}

}
