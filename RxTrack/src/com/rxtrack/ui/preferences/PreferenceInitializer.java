package com.rxtrack.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rxtrack.Activator;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_PROJECTL, "Project:");
		store.setDefault(PreferenceConstants.P_PROJ, "NEA-GRID Health Team 2013");
		store.setDefault(PreferenceConstants.P_ID, "ID#:");
		store.setDefault(PreferenceConstants.P_NAME, "Name:");
		store.setDefault(PreferenceConstants.P_RX, "Rx#:");
		store.setDefault(PreferenceConstants.P_DATE, "Date:");
		store.setDefault(PreferenceConstants.P_DRUG, "DRUG:");
		store.setDefault(PreferenceConstants.P_MITTE, "Mitte:");
		store.setDefault(PreferenceConstants.P_BIN, "BIN:");
		store.setDefault(PreferenceConstants.P_SIG, "SIG:");
		store.setDefault(PreferenceConstants.P_CITY, "Village:");
		store.setDefault(PreferenceConstants.P_RXSTART, 201300000);
		store.setDefault(PreferenceConstants.P_LOGFILE, "logfile");
		store.setDefault(PreferenceConstants.P_PWD, "c:\\temp");
	}

}
