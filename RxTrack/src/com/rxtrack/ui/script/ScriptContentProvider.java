package com.rxtrack.ui.script;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.rxtrack.model.Script;

public class ScriptContentProvider implements IStructuredContentProvider{

	public Object[] getElements(Object inputElement) {
		@SuppressWarnings("unchecked")
		List<Script> a = (List<Script>)inputElement;
		return a.toArray();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

}
