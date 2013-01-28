package com.rxtrack.ui.inventory;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.rxtrack.model.InventoryItem;

public class InventoryContentProvider implements IStructuredContentProvider{

	public Object[] getElements(Object inputElement) {
		@SuppressWarnings("unchecked")
		List<InventoryItem> a = (List<InventoryItem>)inputElement;
		return a.toArray();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

}
