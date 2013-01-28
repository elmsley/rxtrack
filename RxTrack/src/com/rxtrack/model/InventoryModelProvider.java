package com.rxtrack.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InventoryModelProvider {

	private static InventoryModelProvider content;
	private List<InventoryItem> inventoryItems;

	private InventoryModelProvider() {
		refreshItems();
	}

	private void refreshItems(){
		inventoryItems = new ArrayList<InventoryItem>();
		InventoryItem inventoryItem;
		List<DosageListItem> dosageList = MasterModel.getInstance().getDosageList();
		for (DosageListItem dli : dosageList){
			inventoryItem = new InventoryItem(dli);
			inventoryItems.add(inventoryItem);
		}
		
		List<Script> scripts = MasterModel.getInstance().getScriptList();
		Iterator<Script> iter2 = scripts.iterator();
		while (iter2.hasNext()){
			Script s = (Script)iter2.next();
			
			int scriptMitte = 0;
			try {
				scriptMitte = Integer.parseInt(s.getMitte());
			} catch (Exception e) {
				System.out.println("Bad Mitte in script list for " + s.getInventoryItem().getDosage() + ": " + s.getMitte());
			}
			for (int i=0;i<inventoryItems.size();i++){
				InventoryItem ii = (InventoryItem)inventoryItems.get(i);
				if (ii.getDosage().equalsIgnoreCase(s.getInventoryItem().getDosage())){
					ii.setDispensed(ii.getDispensed() + scriptMitte);
				}
			}
			
		}
	}
	
	public static InventoryModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new InventoryModelProvider();
		return content;
	}

	public List<InventoryItem> getInventoryItems() {
		refreshItems();
		return inventoryItems;
	}

}
