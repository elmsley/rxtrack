package com.rxtrack.ui.script;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rxtrack.model.MasterModel;
import com.rxtrack.model.Script;

public class ScriptLabelProvider extends LabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		Script item = (Script)element;
		switch (columnIndex) {
		case 0 : return item.getDate();
		case 1 : return item.getTime();
		case 2 : return item.getPatient().getId();
		case 3 : return item.getPatient().getName();
		case 4 : return item.getInventoryItem().getDosage();
		case 5 : return item.getSig();
		case 6 : return ""+item.getMitte(); 
		case 7 : return item.getRx();
		case 8 : return item.getPics();
		case 9 : return item.getInventoryItem().getBin();
		
		default:
			break;
		}
		return null;
	}

}
