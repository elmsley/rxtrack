package com.rxtrack.exception;

import java.util.List;

import com.rxtrack.model.DosageListItem;

public class LoadException extends Exception {

	/**
	 * serial version uid
	 */
	private static final long serialVersionUID = 112342351243L;
	List<DosageListItem> errorList = null;
	
	public List<DosageListItem> getErrorList() {
		return errorList;
	}

	public LoadException(String message, List<DosageListItem> list){
		super(message);
		errorList = list;
	}
}
