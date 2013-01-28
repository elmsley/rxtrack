package com.rxtrack.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.rxtrack.ui.entry.View;
import com.rxtrack.ui.inventory.InventoryView;
import com.rxtrack.ui.patient.PatientsView;
import com.rxtrack.ui.script.ScriptsView;


public class RxtrackPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		// Top part stacks with (most) everything
//		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.15f, editorArea);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.85f, editorArea);
		folder.addPlaceholder(View.ID);
		folder.addPlaceholder(InventoryView.ID);
		folder.addPlaceholder(PatientsView.ID);
		folder.addView(View.ID);

		// Bottom part is for the script logging
		IFolderLayout folder2 = layout.createFolder("informational", IPageLayout.BOTTOM, 0.15f, editorArea);
//		folder2.addPlaceholder(ScriptsView.ID);
		folder2.addView(ScriptsView.ID);
		
//		layout.getViewLayout(NavigationView.ID).setCloseable(false);


	}
}
