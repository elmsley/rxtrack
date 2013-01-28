package com.rxtrack.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;


import com.rxtrack.ICommandIds;
import com.rxtrack.model.ExcelWriter;

public class ExportInventoryAction extends Action {
	private final IWorkbenchWindow window;
    private IStatusLineManager slmgr;

	public ExportInventoryAction(String text, IWorkbenchWindow window, IStatusLineManager statuslinemgr) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_EXPORT_INV);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_EXPORT_INV);
        setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/Excel-16.gif"));
    	slmgr = statuslinemgr;
    }

    public void run() {
    	DirectoryDialog dd = new DirectoryDialog(window.getShell());
    	dd.setText("Choose Excel to export scripts to:");
    	String dirname = dd.open();
		if (dirname!=null){
			if (ExcelWriter.getInstance().exportInventory(dirname)){
				slmgr.setMessage("Exported to "+dirname+ ".");
			} else {
				MessageDialog.openError(window.getShell(), "Error", "Can't export script to file.");
			}
		}
	}
}
