package com.rxtrack.ui.patient;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.rxtrack.model.Patient;

public class PatientContentProvider implements IStructuredContentProvider{

	public Object[] getElements(Object inputElement) {
		@SuppressWarnings("unchecked")
		List<Patient> a = (List<Patient>)inputElement;
		return a.toArray();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}


}
