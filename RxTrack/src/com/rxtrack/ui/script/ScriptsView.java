package com.rxtrack.ui.script;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;


import com.rxtrack.Activator;
import com.rxtrack.ICommandIds;
import com.rxtrack.model.Script;
import com.rxtrack.model.VBSScriptForRxTrack;
import com.rxtrack.ui.GenericColumnListener;
import com.rxtrack.ui.dialog.PreviewDialog;
import com.rxtrack.ui.entry.View;
import com.rxtrack.ui.preferences.PreferenceConstants;

public class ScriptsView extends ViewPart {

	public static final String ID = ICommandIds.CMD_PREFIX+"ScriptsView";
	private TableViewer tv;

	@Override
	public void createPartControl(Composite parent) {
		tv = new TableViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.MULTI);
		createColumns(tv);
		tv.setContentProvider(new ScriptContentProvider());
		tv.setLabelProvider(new ScriptLabelProvider());
		tv.setInput(ScriptsModelProvider.getInstance().getScriptItems());

		tv.getTable().getColumn(0).addListener(SWT.Selection, new GenericColumnListener(tv, 0));
		tv.getTable().getColumn(1).addListener(SWT.Selection, new GenericColumnListener(tv, 1));
		tv.getTable().getColumn(2).addListener(SWT.Selection, new GenericColumnListener(tv, 2));
		tv.getTable().getColumn(3).addListener(SWT.Selection, new GenericColumnListener(tv, 3));
		tv.getTable().getColumn(4).addListener(SWT.Selection, new GenericColumnListener(tv, 4));
		tv.getTable().getColumn(5).addListener(SWT.Selection, new GenericColumnListener(tv, 5));
		tv.getTable().getColumn(6).addListener(SWT.Selection, new GenericColumnListener(tv, 6));
		tv.getTable().getColumn(7).addListener(SWT.Selection, new GenericColumnListener(tv, 7));
		tv.getTable().getColumn(8).addListener(SWT.Selection, new GenericColumnListener(tv, 8));

		final Menu menuPopup = new Menu(tv.getTable());


		MenuItem itemShow = new MenuItem(menuPopup, SWT.CASCADE);
		itemShow.setText("Print (a&gain)");
		itemShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				if (e.getSource() instanceof MenuItem){
					for (int i=0;i<tv.getTable().getSelection().length;i++){
						Script s = (Script) tv.getTable().getSelection()[i].getData();
						String error = VBSScriptForRxTrack.getInstance().callVbs("p", s);
						if (error!=null && error.trim().length()>0){
							MessageDialog.openError(getSite().getShell(), "Error", error);
						}
					}
				}
			}
		});
		
		itemShow = new MenuItem(menuPopup, SWT.CASCADE);
		itemShow.setText("Pre&view");
		itemShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				if (e.getSource() instanceof MenuItem){
					Script s = (Script) tv.getTable().getSelection()[0].getData();
					String error = VBSScriptForRxTrack.getInstance().callVbs("e", s);
					if (error!=null && error.trim().length()>0){
						MessageDialog.openError(getSite().getShell(), "Error", error);
					}
					try {
						Thread.sleep(1500);
					} catch (Exception e2) {
						// TODO: handle exception
					}
					IPreferenceStore store = null;
					store = Activator.getDefault().getPreferenceStore();
					String temp = store.getString(PreferenceConstants.P_PWD);
					PreviewDialog pd = new PreviewDialog(getSite().getShell());
					pd.setData("title", "Script label for RX: " + s.getRx() + " [" + s.getPatient().getId() + " " + s.getPatient().getName() + "]");
					pd.setData("image", temp + "\\" + s.getPatient().getId() + "_" + s.getRx() +".bmp");
					pd.open();
				}
			}
		});
		
		itemShow = new MenuItem(menuPopup, SWT.CASCADE);
		itemShow.setText("Reuse Pa&tient");
		itemShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				if (e.getSource() instanceof MenuItem){
					Script s = (Script) tv.getTable().getSelection()[0].getData();
					
					IViewReference r []= getSite().getPage().getViewReferences();
					View me = null;
					for (int i=0;i<r.length;i++){
						IWorkbenchPart part = r[i].getPart(true);
						if (part instanceof View){
							me = (View)part;
							me.updatePatient(s.getPatient());
						}
					}
					if (me!=null){
						getSite().getPage().activate(me);
					} else {
						MessageDialog.openError(getSite().getShell(), "Error", "Open Script Entry Window first.");
					}
				}
			}
		});
		
		itemShow = new MenuItem(menuPopup, SWT.CASCADE);
		itemShow.setText("Reuse Dosage");
		itemShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				if (e.getSource() instanceof MenuItem){
					Script s = (Script) tv.getTable().getSelection()[0].getData();
					
					IViewReference r []= getSite().getPage().getViewReferences();
					View me = null;
					for (int i=0;i<r.length;i++){
						IWorkbenchPart part = r[i].getPart(true);
						if (part instanceof View){
							me = (View)part;
							me.updateDosage(s);
						}
					}
					if (me!=null){
						getSite().getPage().activate(me);
					} else {
						MessageDialog.openError(getSite().getShell(), "Error", "Open Script Entry Window first.");
					}
				}
			}
		});
		
		itemShow = new MenuItem(menuPopup, SWT.CASCADE);
		itemShow.setText("Reuse Patient &and Dosage");
		itemShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				Script s = (Script) tv.getTable().getSelection()[0].getData();
				
				IViewReference r []= getSite().getPage().getViewReferences();
				View me = null;
				for (int i=0;i<r.length;i++){
					IWorkbenchPart part = r[i].getPart(true);
					if (part instanceof View){
						me = (View)part;
						me.updateDosage(s);
						me.updatePatient(s.getPatient());
					}
				}
				if (me!=null){
					getSite().getPage().activate(me);
				} else {
					MessageDialog.openError(getSite().getShell(), "Error", "Open Script Entry Window first.");
				}
			}
		});
		
		tv.getTable().setMenu(menuPopup);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		tv.setInput(ScriptsModelProvider.getInstance().getScriptItems());
		tv.refresh();
		setPartName("Scripts: [Total "+ tv.getTable().getItemCount() + "]");
	}
	
	private void createColumns(TableViewer viewer) {
		Font tableFont = new Font(getSite().getShell().getDisplay(), "Arial", 10, SWT.ITALIC);

		String[] titles = { "Date", "Time", "ID", "Name", "Dosage", "SIG", "Mitte", "Rx#", "Pics", "Bin"};
		int[] bounds = { 100, 80, 80, 200, 200, 300, 50, 80, 60, 60 };

		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}
		
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(tableFont);
	}

}
