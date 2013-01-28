package com.rxtrack.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import com.rxtrack.model.InventoryItem;
import com.rxtrack.model.Script;

public class GenericColumnListener implements Listener {
	
	TableViewer tv = null;
	int col = 0;
	int nextDirection = -1;
	
	public GenericColumnListener(TableViewer tv, int col){
		this.tv = tv;
		this.col = col;
	}
	
	public void handleEvent(Event event) {
		TableItem[] items = tv.getTable().getItems();
		Collections.sort(Arrays.asList(items), new Comparator<TableItem>() {
		
			public int compare(TableItem o1, TableItem o2) {
				String d1 = "";
				String d2 = "";
				if (o1.getData() instanceof Script){
					Script a = (Script)o1.getData();
					Script b = (Script)o2.getData();
					switch (col) {
					case 0:
					case 1:
						d1 = a.getDate() + a.getTime();
						d2 = b.getDate() + b.getTime();
						break;
					case 2: 
						d1 = a.getPatient().getId();
						d2 = b.getPatient().getId();
						break;
					case 3:
						d1 = a.getPatient().getName();
						d2 = b.getPatient().getName();
						break;
					case 4:
						d1 = a.getInventoryItem().getDosage();
						d2 = b.getInventoryItem().getDosage();
						break;
					case 5:
						d1 = a.getSig();
						d2 = b.getSig();
						break;
					case 6:
						d1 = a.getMitte();
						d2 = b.getMitte();
						break;
					case 7:
						d1 = a.getRx();
						d2 = b.getRx();
						break;
					case 8:
						d1 = a.getPics();
						d2 = b.getPics();
						break;
					default:
						break;
					}
				} else if (o1.getData() instanceof InventoryItem){
					InventoryItem a = (InventoryItem)o1.getData();
					InventoryItem b = (InventoryItem)o2.getData();
					switch (col) {
					case 0:
						d1 = a.getDosage();
						d2 = b.getDosage();
						break;
					case 1:
						d1 = "" + ((double) a.getDispensed() / (double) a.getInventory());
						d2 = "" + ((double) b.getDispensed() / (double) b.getInventory());
						break;
					case 2:
						d1 = ""+(1000000 + a.getMitte());
						d2 = ""+(1000000 + b.getMitte());
						break;
					case 3:
						d1 = a.getSig();
						d2 = b.getSig();
						break;
					case 4:
						d1 = a.getBin();
						d2 = b.getBin();
						break;
					case 5:
						d1 = a.getPictures();
						d2 = b.getPictures();
						break;
					default:
						break;
					}
				}
				return nextDirection * d1.compareTo(d2);
			}
		});
		try {
			List myList = new ArrayList();
			for (int i=0;i<items.length;i++){
				myList.add(0, items[i].getData());
			}
			tv.setInput(myList);
			nextDirection *= -1;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
