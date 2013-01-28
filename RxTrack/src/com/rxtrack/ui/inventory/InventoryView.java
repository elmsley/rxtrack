package com.rxtrack.ui.inventory;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;


import com.rxtrack.ICommandIds;
import com.rxtrack.actions.MessagePopupAction;
import com.rxtrack.model.InventoryItem;
import com.rxtrack.model.InventoryModelProvider;
import com.rxtrack.ui.GenericColumnListener;

public class InventoryView extends ViewPart {
	
	public static final String ID = ICommandIds.CMD_PREFIX+"InventoryView";
	private TableViewer tv;
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		tv.setInput(InventoryModelProvider.getInstance().getInventoryItems());
		setPartName("Inventory: [Total " +InventoryModelProvider.getInstance().getInventoryItems().size()+" Standard Doses]");
	}
	
	@Override
	public void createPartControl(Composite parent) {
		new MessagePopupAction("something", getViewSite().getWorkbenchWindow(), getViewSite().getActionBars().getStatusLineManager()).run();

//		Composite top = new Composite(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.MULTI);
//		GridLayout gl = new GridLayout();
//		
//		top.setLayout(gl);
//		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		tv = new TableViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.MULTI);
		createColumns(tv);
		tv.setContentProvider(new InventoryContentProvider());
		tv.setLabelProvider(new InventoryLabelProvider());
		tv.setInput(InventoryModelProvider.getInstance().getInventoryItems());

		tv.getTable().getColumn(0).addListener(SWT.Selection, new GenericColumnListener(tv, 0));
		tv.getTable().getColumn(1).addListener(SWT.Selection, new GenericColumnListener(tv, 1));
		tv.getTable().getColumn(2).addListener(SWT.Selection, new GenericColumnListener(tv, 2));
		tv.getTable().getColumn(3).addListener(SWT.Selection, new GenericColumnListener(tv, 3));
		tv.getTable().getColumn(4).addListener(SWT.Selection, new GenericColumnListener(tv, 4));
		tv.getTable().getColumn(5).addListener(SWT.Selection, new GenericColumnListener(tv, 5));

		Listener paintListener = new Listener() {
			public void handleEvent(Event event) {
				switch(event.type) {
					case SWT.PaintItem: {
						if (event.index == 1) {
							GC gc = event.gc;
							TableItem item = (TableItem)event.item;
							InventoryItem ii = ((InventoryItem)(item.getData()));
							int percent = (int)((double)(ii.getDispensed() / (double)ii.getInventory()) * 100);
							Color foreground = gc.getForeground();
							Color background = gc.getBackground();
							if (percent<60){
								gc.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_GREEN));
							} else if (percent<80){
								gc.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
							} else {
								gc.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
							}
							gc.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_GRAY));
							double width2 = (tv.getTable().getColumns()[1].getWidth() - 1) * percent / 100;
							int width = (int)width2;
							gc.fillGradientRectangle(event.x, event.y, width, event.height, true);					
							Rectangle rect2 = new Rectangle(event.x, event.y, width-1, event.height-1);
							gc.drawRectangle(rect2);
							gc.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
							String text = percent+"%" + "(" +ii.getDispensed() + "/" + ii.getInventory() + ")";
							Point size = event.gc.textExtent(text);					
							int offset = Math.max(0, (event.height - size.y) / 2);
							gc.drawText(text, event.x+2, event.y+offset, true);
							gc.setForeground(background);
							gc.setBackground(foreground);
						}
						break;
					}
				}
			}
		};		
		tv.getTable().addListener(SWT.PaintItem, paintListener);

	}
	
	// This will create the columns for the table
	private void createColumns(TableViewer viewer) {
		Font tableFont = new Font(getSite().getShell().getDisplay(), "Arial", 10, SWT.ITALIC);
		
		String[] titles = { "Dosage", "Inventory", "Mitte", "SIG", "Bin", "Pictures" };
		int[] bounds = { 400, 150, 70, 300, 100, 80 };

		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
		}
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(tableFont);
	}

}
