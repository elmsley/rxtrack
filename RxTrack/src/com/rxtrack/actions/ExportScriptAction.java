package com.rxtrack.actions;

import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;


import com.rxtrack.ICommandIds;
import com.rxtrack.model.ExcelWriter;
import com.rxtrack.util.RxUtil;

public class ExportScriptAction extends Action {
    private final IWorkbenchWindow window;
    private IStatusLineManager slmgr;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public ExportScriptAction(String text, IWorkbenchWindow window, IStatusLineManager statuslinemgr) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_EXPORT_SCRIPTS);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_EXPORT_SCRIPTS);
        setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/Excel-16.gif"));
    	slmgr = statuslinemgr;
    }

    public void run() {
    	DirectoryDialog dd = new DirectoryDialog(window.getShell());
    	dd.setText("Choose Excel directory to export scripts to:");
    	String dirname = dd.open();
		if (dirname!=null){
			String exportedFile = ExcelWriter.getInstance().exportScripts(dirname);
			if (!RxUtil.isEmpty(exportedFile)){
				String message = "Exported to "+exportedFile+ ".";
				slmgr.setMessage(message);
				LOGGER.info(message);
			} else {
				MessageDialog.openError(window.getShell(), "Error", "Can't export script to file to " + dirname + ".");
			}
		}
	}
}
