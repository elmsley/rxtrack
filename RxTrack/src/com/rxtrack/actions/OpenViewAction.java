package com.rxtrack.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.rxtrack.Activator;
import com.rxtrack.ui.entry.View;
import com.rxtrack.ui.inventory.InventoryView;
import com.rxtrack.ui.patient.PatientsView;



public class OpenViewAction extends Action {
	
	private final IWorkbenchWindow window;
	private int instanceNum = 0;
	private final String viewId;
	private boolean _hasMultiple = false;
	
	public OpenViewAction(IWorkbenchWindow window, String label, String viewId, String commandid, boolean hasMultiple) {
		_hasMultiple = hasMultiple;
		this.window = window;
		this.viewId = viewId;
        setText(label);
        // The id is used to refer to the action in a menu or toolbar
		setId(commandid);
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(commandid);
		setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/storage.gif"));
		if (viewId.equals(PatientsView.ID)){
			setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/patient.png"));
		} else if (viewId.equals(View.ID)){
			setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/script.gif"));
		} else if (viewId.equals(InventoryView.ID)){
			setImageDescriptor(com.rxtrack.Activator.getImageDescriptor("/icons/inventory.gif"));
		}
	}
	
	public void run() {
		if(window != null) {	
			try {
				window.getActivePage().showView(viewId, _hasMultiple ? Integer.toString(instanceNum++) : null, IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
