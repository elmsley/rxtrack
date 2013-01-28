package com.rxtrack.ui.patient;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rxtrack.model.Patient;

public class PatientLabelProvider extends LabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		Patient item = (Patient)element;
		switch (columnIndex) {
		case 0 : return item.getId();
		case 1 : return item.getName();
		case 3 : return item.getCity();
		default:
			break;
		}
		return null;
	}



}
