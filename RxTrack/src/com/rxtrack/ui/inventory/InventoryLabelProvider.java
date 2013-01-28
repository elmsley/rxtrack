package com.rxtrack.ui.inventory;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rxtrack.model.InventoryItem;
import com.rxtrack.model.ReadExcel;

public class InventoryLabelProvider extends LabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		InventoryItem item = (InventoryItem) element;
		switch (columnIndex) {
		case ReadExcel.C_Dosage: return item.getDosage();
		case ReadExcel.C_Bin: return item.getBin();
		case ReadExcel.C_Inventory: return "";//+item.getInventory();
		case ReadExcel.C_Mitte: return ""+item.getMitte();
		case ReadExcel.C_Pictures: return item.getPictures();
		case ReadExcel.C_SIG: return item.getSig();
		default:
			break;
		}
		return null;
	}

}
