package com.rxtrack.actions;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;


import com.rxtrack.Activator;
import com.rxtrack.ICommandIds;
import com.rxtrack.model.DosageListItem;
import com.rxtrack.model.MasterModel;
import com.rxtrack.model.ReadExcel;
import com.rxtrack.ui.preferences.PreferenceConstants;


public class MessagePopupAction extends Action {

    private final IWorkbenchWindow window;
    private IStatusLineManager slmgr;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public MessagePopupAction(String text, IWorkbenchWindow window, IStatusLineManager statuslinemgr) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
        setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/reload.gif"));
    	slmgr = statuslinemgr;
    }

    public void run() {
    	String dfile = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_DOSAGEFILE);
    	String message = "";
		if (dfile!=null && dfile.trim().length()>0){
			List<List<DosageListItem>> myList = null;
	    	myList = new ReadExcel().loadRxTrackFile(dfile);
	    	String extra = "";
	    	if (myList==null){
	    		message = "Valid Dosage File needed.  Check Preferences.";
	    		slmgr.setMessage(message);
	    		LOGGER.info(message);
	    		return;
	    	}
	    	if (myList.size()>1){
				StringBuffer sb = new StringBuffer();
				sb.append("Error processing standard dosage list found in (" + dfile).append(")\n");
				sb.append("-------------------------------------------------------------------\n");
				for (DosageListItem dli : myList.get(1)){
					sb.append(dli.getDosage()).append("\n");
				}
				sb.append("-------------------------------------------------------------------\n");
				sb.append("To ignore, click OK.  Please ensure you check the standard dosage file for syntax errors.");
				MessageDialog.openWarning(window.getShell(), "Error Processing File", sb.toString());
				extra = myList.get(1).size() + " row(s) skipped from file.";
	    	}
	        MasterModel.getInstance().setDosageList(myList.get(0));
	        message = "Loaded "+ myList.get(0).size() +" row(s) on " +dfile+" successfully! " + extra;
			
		} else {
			MessageDialog.openWarning(window.getShell(), "Error", "Error loading dosage file, please check Window->Preferences!");
			message = "Error loading dosage file!";
		}
        slmgr.setMessage(message);
		LOGGER.info(message);
	}
}