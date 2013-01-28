package com.rxtrack.ui.dialog;

import java.util.List;

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

import com.rxtrack.model.DosageListItem;
import com.rxtrack.model.MasterModel;

public class SelectDrugDialog extends Dialog {
	String selectedDrug = "";
	
	Image id = ImageDescriptor.createFromURL(getClass().getResource("/icons/loading.png")).createImage();
	
	protected FilteredTree filteredTree;
	
	public SelectDrugDialog(Shell parentShell, String drug) {
		super(parentShell);
		setShellStyle(getShellStyle()|SWT.RESIZE);
		this.selectedDrug = drug;
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
		viewer.setContentProvider(new DrugTreeContentProvider());
		viewer.setLabelProvider(new DrugLabelProvider());
		viewer.setInput(MasterModel.getInstance().getDosageList());

		// when double click on item selected and close dialog
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(viewer.getTree().getSelection().length > 0)
				{
					selectedDrug = viewer.getTree().getSelection()[0].getText();
				}
				okPressed();
			}
		});
		
		// update drug name when selection change
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(viewer.getTree().getSelection().length > 0)
				{
					selectedDrug = viewer.getTree().getSelection()[0].getText();
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
						selectedDrug = topItem.getText();
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
		
		// select old drug
		for(TreeItem item : viewer.getTree().getItems()) {
			if (item.getText().equals(selectedDrug)) {
				viewer.getTree().select(item);
				break;
			}
		}
		return composite;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select Drug.  Hit ESC to cancel.");
		newShell.setSize(500, 600);
	}	
	
	/**
	 * @return selected drug
	 */
	public String getSelectedDrug() {
		return selectedDrug;
	}
	
	public Point getSelectedDrugIndex(){
		List<DosageListItem> dlis = MasterModel.getInstance().getDosageList();
		for (int i=0;i<dlis.size();i++){
			if (dlis.get(i).getDosage().equals(selectedDrug)){
				return new Point(0, i);
			}
		}
		return null;
	}
	
	/**
	 * Drug content provider
	 */
	private class DrugTreeContentProvider implements ITreeContentProvider {

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
			List<DosageListItem> dli = (List<DosageListItem>)inputElement;
			return dli.toArray(new DosageListItem[0]);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object element1, Object element2) {
		}
	}
	
	/**
	 * Drug label provider
	 */
	private class DrugLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			DosageListItem dli = (DosageListItem) element; 
			return dli.getDosage();
		}
		@Override
		public Image getImage(Object element) {
			
			DosageListItem dli = (DosageListItem) element; 
			char firstLetter = dli.getDosage().charAt(0);
			char runningChar = '0';
			if ((firstLetter-runningChar) % 2 == 0){
				return id;
			}
			/*
			for (DosageListItem dliOne : MasterModel.getInstance().getDosageList()){
				char currChar = dliOne.getDosage().charAt(0); // A, A, A, B..
				if (runningChar!=currChar){
					runningChar = currChar;
					if (runningChar==firstLetter && dliOne.getDosage().equals(dli.getDosage())){
						return id;
					}
				}
			}
			*/
			return null;
		}
	}

}
