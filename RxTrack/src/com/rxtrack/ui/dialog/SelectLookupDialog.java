package com.rxtrack.ui.dialog;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.rxtrack.model.Lookups;

public class SelectLookupDialog extends Dialog {
	String selected = "";
	String sheet = "";
	
	Image id = ImageDescriptor.createFromURL(getClass().getResource("/icons/loading.png")).createImage();
	
	protected FilteredTree filteredTree;
	
	public SelectLookupDialog(Shell parentShell, String value, String citySheet) {
		super(parentShell);
		setShellStyle(getShellStyle()|SWT.RESIZE);
		this.selected = value;
		sheet = citySheet;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(1,false));

		filteredTree = new FilteredTree(composite,
										SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL,
										new PatternFilter(),
										true);
		final TreeViewer viewer = filteredTree.getViewer();
		viewer.setContentProvider(new GenericTreeContentProvider());
		viewer.setLabelProvider(new GenericLabelProvider());
		viewer.setInput(Lookups.getInstance().getLookup(sheet));

		// when double click on item selected and close dialog
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(viewer.getTree().getSelection().length > 0)
				{
					selected = viewer.getTree().getSelection()[0].getText();
				}
				okPressed();
			}
		});
		
		// update drug name when selection change
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(viewer.getTree().getSelection().length > 0)
				{
					selected = viewer.getTree().getSelection()[0].getText();
				}
			}
		});
		
		// force selection of first element when focus moves to viewer
		viewer.getTree().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (viewer.getTree().getSelection()==null){
					TreeItem topItem = viewer.getTree().getTopItem();
					if(topItem != null) {
						viewer.getTree().select(topItem);
						selected = topItem.getText();
					}
				}
				super.focusGained(e);
			}
		});
		
		// we need to enable/disable OK button if no items were found
		viewer.getTree().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				getButton(OK).setEnabled(viewer.getTree().getItems().length > 0);
			}
		});
		
		// select old selection
		for(TreeItem item : viewer.getTree().getItems()) {
			if (item.getText().equals(selected)) {
				viewer.getTree().select(item);
				break;
			}
		}
		return composite;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Choose.  Hit ESC to cancel.");
		newShell.setSize(300, 400);
	}	
	
	/**
	 * @return selected
	 */
	public String getselected() {
		return selected;
	}
	
	public Point getselectedIndex(){
		Set<String> dlis = Lookups.getInstance().getLookup(sheet);
		int i=0;
		for (String str : dlis.toArray(new String[0])){
			if (str.equals(selected)){
				return new Point(0, i);
			}
			i++;
		}
		
		return null;
	}
	
	/**
	 * Drug content provider
	 */
	private class GenericTreeContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object element) {
			return null;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return false;
		}

		public Object[] getElements(Object inputElement) {
			Set<String> set = (Set<String>)inputElement;
			return set.toArray(new String[0]);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object element1, Object element2) {
		}
	}
	
	/**
	 * Drug label provider
	 */
	private class GenericLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			return element.toString();
		}
		@Override
		public Image getImage(Object element) {
			return id;
		}
	}

}
