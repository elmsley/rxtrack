package com.rxtrack.model;

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rxtrack.Activator;
import com.rxtrack.ui.preferences.PreferenceConstants;


/**
 * Represents default pictures configuration for a drug.
 * @author jasonmah
 *
 */
public class Picture {
	
	public enum IconPosition {
		Dosage1("1"), Dosage2("2"), Dosage3("3"),
		Frequency4("4"), Frequency5("5"), Frequency6("6"), Frequency7("7"),
		Other8("8"), Other9("9");
		private String code;
		IconPosition(String code){
			this.code = code;
		}
		public String getCode(){
			return code;
		}
	}
	// variations on dosage
	public final String DOSAGETYPES = "CDEGHILMNPSTUVW";
	
	// variations on other icons
	final String OTHERTYPES = "X";
	
	private String userSpecified = "";
	private char dosageType = 'T';
	private char otherType = 'A';
	public final String DIR_PREFIX = "/images/";

	/**
	 * Return the eclipse path to the image at this position
	 * @param iconPosition
	 * @return
	 */
	public String getImage(IconPosition iconPosition){
		return DIR_PREFIX + getShortImageName(iconPosition);
	}
	
	/*
	 * Raw file name
	 */
	private String getShortImageName(IconPosition iconPosition){
		switch (iconPosition) {
		case Dosage1:
			return "once"+dosageType+".bmp";
		case Dosage2:
			return "twice"+dosageType+".bmp";
		case Dosage3:
			return "thrice"+dosageType+".bmp";
		case Frequency4:
			return "morning.bmp";
		case Frequency5:
			return "lunch.bmp";
		case Frequency6:
			return "evening.bmp";
		case Frequency7:
			return "night.bmp";
		case Other8:
			return "food.bmp";
		case Other9:
			if (otherType == 'X'){
				return "pregnant.bmp";
			}
			return "kid100.bmp";
		default:
			break;
		}
		return "";
	}
	
	/**
	 * returns the full path on disk for the icon position selected.  The current configuration
	 * is what the user selected based on the base configuration available to them.  Blank images
	 * will be returned if they didn't choose anything.
	 * @param currentConfiguration
	 * @param iconPosition
	 * @return
	 */
	public String getTempImagePath(String currentConfiguration, IconPosition iconPosition){
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String pwd = store.getString(PreferenceConstants.P_PWD);
		
		pwd += "\\rxtrack\\";
		if (currentConfiguration.contains(iconPosition.getCode())){
			return pwd + getShortImageName(iconPosition);
		}
		return pwd + "blank100x100.bmp";
	}
	
	public String getState(IconPosition iconPosition){
		if (userSpecified.indexOf(iconPosition.getCode())>=0){
			return "on";
		}
		return "off";
	}
	
	public String getUserSpecified() {
		return userSpecified;
	}

	public void setUserSpecified(String userSpecified) {
		this.userSpecified = userSpecified;
		for (char a : DOSAGETYPES.toCharArray()){
			if (userSpecified.indexOf(a)>=0){
				dosageType = a;
				break;
			}
		}
		
		for (char a : OTHERTYPES.toCharArray()){
			if (userSpecified.indexOf(a)>=0){
				otherType = a;
				break;
			}
		}
		
	}

	public String getNewSerializedValue(Map<IconPosition, Boolean> buttonsOnOff) {
		StringBuffer sb = new StringBuffer(); 
		for (IconPosition ip : buttonsOnOff.keySet()){
			if (buttonsOnOff.get(ip)){
				sb.append(ip.getCode());
			}
		}
		sb.append(dosageType);
		if (otherType!='A')
			sb.append(otherType);
		return sb.toString();
	}
	
}
