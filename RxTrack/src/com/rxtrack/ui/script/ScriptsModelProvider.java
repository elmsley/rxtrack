package com.rxtrack.ui.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rxtrack.model.MasterModel;
import com.rxtrack.model.Script;

public class ScriptsModelProvider {
	private static ScriptsModelProvider content;
	private List<Script> scriptItems;

	private ScriptsModelProvider() {
		refreshItems();
	}

	private void refreshItems(){
		scriptItems = new ArrayList<Script>();
		List<Script> dosageList = MasterModel.getInstance().getScriptList();
		Iterator<Script> iter = dosageList.iterator();
		while (iter.hasNext()){
			Script row = (Script)iter.next();
			scriptItems.add(row);
		}
	}
	
	public static synchronized ScriptsModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new ScriptsModelProvider();
		return content;
	}

	public List<Script> getScriptItems() {
		refreshItems();
		return scriptItems;
	}

}
