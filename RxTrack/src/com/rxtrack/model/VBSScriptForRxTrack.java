package com.rxtrack.model;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;


import com.rxtrack.Activator;
import com.rxtrack.model.Picture.IconPosition;
import com.rxtrack.ui.preferences.PreferenceConstants;

public class VBSScriptForRxTrack {
	private static VBSScriptForRxTrack _instance = null;
	public static VBSScriptForRxTrack getInstance(){
		if (_instance==null){
			_instance = new VBSScriptForRxTrack();
		}
		return _instance;
	}
	
	protected String addArgument(String orig, String append){
		return orig += " \"" + append + "\"";
	}
	
	public String callVbs(String flag, Script s){
		try {
			IPreferenceStore store = null;
			store = Activator.getDefault().getPreferenceStore();
			String pwd = store.getString(PreferenceConstants.P_PWD);
			
			// print label
			String arguments = flag;
			String tempExport = pwd + "\\" + s.getPatient().getId() + "_" + s.getRx() +".bmp";
			
			arguments = addArgument(arguments, tempExport); // exported file
			arguments = addArgument(arguments, s.getPatient().getId() + "_" + s.getRx()); // print job
			String labelfile = store.getString(PreferenceConstants.P_LABELFILE);
			if (labelfile==null || labelfile.trim().length()==0 || !(new File(labelfile).exists())){
				return "Bad or Missing label file.  Please check Windows->Preferences";
			}
			arguments = addArgument(arguments, labelfile); // label file
			arguments = addArgument(arguments, store.getString(PreferenceConstants.P_PROJ)); 
	
			String name2 = s.getPatient().getName()==null || s.getPatient().getName().trim().length()==0 ? "Name:______________________" : s.getPatient().getName();
			arguments = addArgument(arguments, name2);
			arguments = addArgument(arguments, s.getSig());
			arguments = addArgument(arguments, s.getMitte());	
			String dos = s.getInventoryItem().getDosage();
			arguments = addArgument(arguments, dos);
			arguments = addArgument(arguments, s.getDate());
			arguments = addArgument(arguments, s.getInventoryItem().getBin());
			
			Picture picture = new Picture();
			picture.setUserSpecified(s.getInventoryItem().getPictures());
			// images
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Dosage1));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Dosage2));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Dosage3));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Frequency4));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Frequency5));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Frequency6));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Frequency7));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Other8));
			arguments = addArgument(arguments, picture.getTempImagePath(s.getPics(), IconPosition.Other9));
			
			arguments = addArgument(arguments, pwd + "\\rxtrack\\jar.bmp");
			
			String cmd = pwd.replace("\\\\", "/") + "/rxtrack/CreateLabel.vbs";
			
//			Runtime.getRuntime().exec("cmd.exe /C echo "+cmd+ " > c:\\temp\\abc.txt");
//			Runtime.getRuntime().exec("cmd.exe /C echo "+arguments+ " >> c:\\temp\\abc.txt");
			
			Process p = Runtime.getRuntime().exec("cmd.exe /C " + cmd + " "+arguments);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO: handle exception
			}

//			String tempExport2 = temp + "\\" + s.getPatient().getId() + "_" + s.getRx() +"x.bmp";
//			Process p2 = Runtime.getRuntime().exec("C:\\Program Files\\ImageMagick-6.6.4-Q16\\convert -resize 703x439 " + tempExport + " " + tempExport2);
			  
			return null;
		} catch (Exception e2) {
			return e2.getLocalizedMessage();
		}
	}
}
