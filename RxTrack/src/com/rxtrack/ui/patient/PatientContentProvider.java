package com.rxtrack.ui.patient;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.rxtrack.model.Patient;

public class PatientContentProvider implements IStructuredContentProvider{

	public Object[] getElements(Object inputElement) {
		@SuppressWarnings("unchecked")
		List<Patient> original = (List<Patient>)inputElement;
		List<Patient> unique = new ArrayList<Patient>();
		for (Patient p : original){
			if (!unique.contains(p)){
				unique.add(p);
			} else {
				// not unique
//				System.out.println("Duplicate Patient: "+p.getId() + " " + p.getName());
			}
		}
		return unique.toArray();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}


}
