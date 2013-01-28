package com.rxtrack;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.ISources;

public class AbstractSourceProvider extends
		org.eclipse.ui.AbstractSourceProvider {

	public AbstractSourceProvider() {
		// TODO Auto-generated constructor stub
	}

	public final static String MY_STATE = ICommandIds.CMD_PREFIX+"variable1";
	public final static String ENABLED = "ENABLED";
	public final static String DISENABLED = "DISENABLED";
	private boolean enabled = true;

	// This method can be used from other commands to change the state
	// Most likely you would use a setter to define directly the state and not use this toogle method
	// But hey, this works well for my example
	public void toogleEnabled() {
		enabled = !enabled ;
		String value = enabled ? ENABLED : DISENABLED;
		fireSourceChanged(ISources.WORKBENCH, MY_STATE, value);
	}

	public void dispose() {
		
	}

	public String[] getProvidedSourceNames() {
		return new String[] { MY_STATE };

	}
	
	// You cannot return NULL
//	@SuppressWarnings("unchecked")
//	@Override
	public Map getCurrentState() {
		Map map = new HashMap(1);
		String value = enabled ? ENABLED : DISENABLED;
		map.put(MY_STATE, value);
		return map;
	}


}
