package com.rxtrack.ui.patient;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;


import com.rxtrack.ICommandIds;
import com.rxtrack.model.MasterModel;

public class PatientsView extends ViewPart {

	public static final String ID = ICommandIds.CMD_PREFIX+"PatientsView";
	private TableViewer tv;

	@Override
	public void createPartControl(Composite parent) {
		tv = new TableViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.MULTI);
		createColumns(tv);
		tv.setContentProvider(new PatientContentProvider());
		tv.setLabelProvider(new PatientLabelProvider());
		tv.setInput(MasterModel.getInstance().getPatientList());
		
	}

	@Override
	public void setFocus() {
		tv.setInput(MasterModel.getInstance().getPatientList());
		tv.refresh();

	}
	
	private void createColumns(TableViewer viewer) {
		Font tableFont = new Font(getSite().getShell().getDisplay(), "Arial", 10, SWT.ITALIC);
		String[] titles = { "ID", "Name", "Address", "City/Town", "State", "Country", "PC/Zipcode", "Date of Birth", "Sex" };
		int[] bounds = { 80, 200, 200, 100, 75, 75, 75, 100, 50 };

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
