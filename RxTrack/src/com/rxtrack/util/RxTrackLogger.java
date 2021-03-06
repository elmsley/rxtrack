package com.rxtrack.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rxtrack.Activator;
import com.rxtrack.ui.preferences.PreferenceConstants;

public class RxTrackLogger {
		  static private FileHandler fileTxt;
		  static private SimpleFormatter formatterTxt;

		  static public void setup() throws IOException {

		    // Get the global logger to configure it
		    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		    logger.setLevel(Level.INFO);
		    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		    String logfile = store.getString(PreferenceConstants.P_PWD) + "\\trace.log";
		    
		    fileTxt = new FileHandler(logfile);

		    // Create txt Formatter
		    formatterTxt = new SimpleFormatter();
		    fileTxt.setFormatter(formatterTxt);
		    fileTxt.setLevel(Level.INFO);
		    logger.addHandler(fileTxt);

		    LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.INFO);
		  }
}
