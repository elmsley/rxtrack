package com.rxtrack.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rxtrack.Activator;
import com.rxtrack.ui.preferences.PreferenceConstants;


public class MasterModel {

	private static MasterModel _instance = null;
	
	private MasterModel(){
	}
	
	protected List<DosageListItem> dosageList = new ArrayList<DosageListItem>();
	protected List<Script> scriptList = new ArrayList<Script>();
	protected List<Patient> patientList = new ArrayList<Patient>();
	
	public List<Patient> getPatientList() {
		return patientList;
	}

	public void setPatientList(List<Patient> patientList) {
		this.patientList = patientList;
	}

	public List<Script> getScriptList() {
		return ScriptPersist.getInstance().load();
	}

	public void setScriptList(List<Script> scriptList) {
		this.scriptList = scriptList;
	}

	public List<DosageListItem> getDosageList() {
		return dosageList;
	}

	public void setDosageList(List<DosageListItem> dosageList) {
		this.dosageList = dosageList;
	}

	public static MasterModel getInstance(){
		if (_instance==null){
			_instance = new MasterModel();
		}
		return _instance;
	}
	
	public int getNextRX(){
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		int next = 1+store.getInt(PreferenceConstants.P_RXSTART);
		store.setValue(PreferenceConstants.P_RXSTART, next);
		return next;
	}
	
}
