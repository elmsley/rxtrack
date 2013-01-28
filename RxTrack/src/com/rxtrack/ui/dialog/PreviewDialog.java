package com.rxtrack.ui.dialog;

import java.io.FileInputStream;
import java.util.HashMap;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PreviewDialog extends TitleAreaDialog {

	public PreviewDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
			
		setTitle(h.get("title"));
		setMessage("This is what the label will look like when printed.  The image is also saved on the filesystem as: [ " + h.get("image") + " ]");
		parent.getLayout();
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 800;
		gd.heightHint = 500;
		comp.setLayoutData(gd);
		
		Canvas canvas = new Canvas(comp, SWT.PUSH);
		
		final Display d = getShell().getDisplay();
		try {
			gd = new GridData();
			gd.widthHint = 800;
			gd.heightHint = 500;
			gd.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
			gd.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
			canvas.setLayoutData(gd);
			canvas.pack();
			
			canvas.addPaintListener(new PaintListener() {
			      public void paintControl(PaintEvent e) {
			    	  try {
					        Image image = new Image(d, new FileInputStream(h.get("image")));
					        e.gc.drawImage(image, 0, 0);
					        image.dispose();
						
					} catch (Exception e2) {
						e2.printStackTrace();
					}
			      }
			    });
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comp;
	}
	
	protected HashMap<String, String> h = new HashMap<String, String>();
	
	public void setData(String key, String value){
		h.put(key, value);
	}
}
