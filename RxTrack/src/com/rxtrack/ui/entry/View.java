package com.rxtrack.ui.entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.rxtrack.Activator;
import com.rxtrack.ICommandIds;
import com.rxtrack.actions.MessagePopupAction;
import com.rxtrack.model.ConversionRangeRow;
import com.rxtrack.model.DosageListItem;
import com.rxtrack.model.InventoryItem;
import com.rxtrack.model.InventoryModelProvider;
import com.rxtrack.model.MasterModel;
import com.rxtrack.model.Patient;
import com.rxtrack.model.Picture;
import com.rxtrack.model.Script;
import com.rxtrack.model.ScriptPersist;
import com.rxtrack.model.VBSScriptForRxTrack;
import com.rxtrack.model.XLSConstants;
import com.rxtrack.model.Picture.IconPosition;
import com.rxtrack.ui.dialog.PreviewDialog;
import com.rxtrack.ui.dialog.SelectDrugDialog;
import com.rxtrack.ui.dialog.SelectLookupDialog;
import com.rxtrack.ui.preferences.PreferenceConstants;
import com.rxtrack.util.RxUtil;

public class View extends ViewPart {

	protected Image c1Image = null;
	protected Image offImage = null;
	public static final String ID = ICommandIds.CMD_PREFIX+"view";
	protected Text mitte = null;
	protected Text bin = null;
	protected Text sig = null;
	protected Text id = null;
	protected Text name = null;
	protected Text city = null;
	protected Text rx = null;
	protected Text date = null;
	protected Combo drug = null;
	protected Button c1, c2, c3;
	protected Button co1, co2;
	protected Button w1, w2, w3, w4;
	protected TabFolder stack;
	protected TabItem lookup;
	protected TabItem conversion;
	protected Text mkd;
	protected Text concentration;
	protected Text weight;
	protected String sigOrig;
	protected Table lookupTable;
	protected Composite formula;
	protected Color labelColor;
	protected Color blackColor;
	protected Color greyColor;
	protected Color redColor;
	protected String pictures = "";
	protected String flags = "";
	protected List<Button> buttons = new ArrayList<Button>();

	protected IPreferenceStore store = null;
	
	private Font tableFont = null;
	private Font formulaFont = null;
	private Font boldFont = null;

	public void createPartControl(Composite parent) {

		redColor = new Color(getViewSite().getShell().getDisplay(), 255, 127, 127);
		labelColor = new Color(getViewSite().getShell().getDisplay(), 64, 64, 255);
		blackColor = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		greyColor = parent.getDisplay().getSystemColor(SWT.COLOR_GRAY);

		// Check script file and popup warnings.
		new MessagePopupAction("something", getViewSite().getWorkbenchWindow(), getViewSite().getActionBars().getStatusLineManager()).run();

		store = Activator.getDefault().getPreferenceStore();
		setPartName(getPartName());

		Composite top = createTop(parent);
		createButtonsAndFormula(top);
		
		clearForNewScript();

		new RxUtil().createTempImages();
	}
	
	private Composite createButtonsAndFormula(Composite top) {
		Composite buttonsAndFormula = new Composite(top, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 2;
		buttonsAndFormula.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;

		buttonsAndFormula.setLayoutData("span 10");

		createAllButtonsGroup(buttonsAndFormula);
		formula = createFormulaGroup(buttonsAndFormula);
		return buttonsAndFormula;
	}

	private Composite createFormulaGroup(Composite buttonsAndFormula) {
		Composite formula = new Composite(buttonsAndFormula, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 1;
		formula.setLayout(layout);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		formula.setLayoutData(gd);
		
		stack = new TabFolder(formula, SWT.None);
		stack.setLayout(new GridLayout());
		stack.setFont(tableFont);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 525;
		gd.heightHint = 450;
		stack.setLayoutData(gd);

		lookup = new TabItem(stack, SWT.NONE);
		lookup.setText("Lookup");
//		org.eclipse.swt.widgets.List aList = new org.eclipse.swt.widgets.List(stack, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
//		aList.setItems(new String[] {"N/A"});
//		lookup.setControl(aList);

		lookupTable = new Table(stack, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		TableColumn tc = new TableColumn(lookupTable, SWT.NONE);
		tc.setText("Range Min");
		tc = new TableColumn(lookupTable, SWT.NONE);
		tc.setText("Range Max");
		tc = new TableColumn(lookupTable, SWT.NONE);
		tc.setText("Replacement");
		lookupTable.getColumn (0).pack ();
		lookupTable.getColumn (1).pack ();
		lookupTable.getColumn (2).pack ();
		lookupTable.getColumn (0).setWidth(175);
		lookupTable.getColumn (1).setWidth(175);
		lookupTable.getColumn (2).setWidth(175);
		lookupTable.setLinesVisible(true);
		lookupTable.setHeaderVisible(true);
		lookupTable.setFont(tableFont);

		lookupTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				String rep = "_";
				if (e.item instanceof TableItem){
					TableItem ti = (TableItem)e.item;
					rep = ti.getText(2);
				}
				String txt = sigOrig;
				txt = txt.replace("_", ""+rep);
				sig.setText(txt);
			}
		});

		lookup.setControl(lookupTable);

		conversion = new TabItem(stack, SWT.NONE);
		conversion.setText("Dosing Calc");
		Composite aFormula = new Composite(stack, SWT.None);
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;
		aFormula.setLayout(gl);

		Composite f1 = new Composite(aFormula, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		f1.setLayout(gl);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		f1.setLayoutData(gd);

		Composite frow = new Composite(f1, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		frow.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		frow.setLayoutData(gd);

		mkd = new Text(frow, SWT.NONE);
		gd = new GridData();
		gd.minimumWidth = 50;
		gd.widthHint = 40;
		mkd.setLayoutData(gd);
		mkd.setFont(formulaFont);
		mkd.setBackground(redColor);
		mkd.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent event) {
				// TODO Auto-generated method stub
				try {
					if ((event.character == '\u0008') || (event.character == '\u007F') || event.character == '.') {
						// allow
					} else {
						if ("".equals(event.text)){

						} else {
							Double.parseDouble(event.text);
						}
					}
				} catch (Exception e2) {
					event.doit = false;
				}
			}
		});
		mkd.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					double a = Double.parseDouble(mkd.getText());
					double b = Double.parseDouble(weight.getText());
					double c = Double.parseDouble(concentration.getText());
					double d = a * b / c;
					String txt = sigOrig;
					txt = txt.replace("_", ""+d);
					sig.setText(txt);
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		Label tempLabel = new Label(frow, SWT.NONE);
		tempLabel.setText("mg/kg/dose");
		tempLabel.setFont(formulaFont);

		tempLabel = new Label(f1, SWT.NONE);
		tempLabel.setText("______________________");
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		tempLabel.setLayoutData(gd);

		frow = new Composite(f1, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		frow.setLayout(gl);

		concentration = new Text(frow, SWT.BORDER);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 35;
//		gd.horizontalAlignment = GridData.END;
		concentration.setLayoutData(gd);
		concentration.setEditable(false);
		concentration.setFont(formulaFont);
		tempLabel = new Label(frow, SWT.NONE);
		tempLabel.setText("mg/mL (concentration)");
		tempLabel.setFont(formulaFont);
//		frow = new Composite(f1, SWT.NONE);
//		gl = new GridLayout();
//		gl.numColumns = 1;
//		frow.setLayout(gl);
//		new Label(frow, SWT.NONE).setText("(concentration)");

		tempLabel = new Label(aFormula, SWT.None);
		tempLabel.setText(" x ");
		tempLabel.setFont(formulaFont);
		gd = new GridData();
//		gd.verticalAlignment = GridData.BEGINNING;
		tempLabel.setLayoutData(gd);
		conversion.setControl(aFormula);

		f1 = new Composite(aFormula, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 1;
		f1.setLayout(gl);
		frow = new Composite(f1, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		frow.setLayout(gl);
		weight = new Text(frow, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 35;
//		gd.minimumWidth = 50;
		weight.setLayoutData(gd);
		weight.setBackground(redColor);
		tempLabel = new Label(frow, SWT.NONE);
		tempLabel.setText("kg");
		tempLabel.setFont(formulaFont);
		tempLabel = new Label(f1, SWT.NONE);
		tempLabel.setText("(weight)");
		tempLabel.setFont(formulaFont);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		tempLabel.setLayoutData(gd);

		gl = new GridLayout();
		tempLabel = new Label(aFormula, SWT.NONE);
		tempLabel.setText("= (dose in mL)");
		tempLabel.setFont(formulaFont);
		gd = new GridData();
//		gd.verticalAlignment = GridData.BEGINNING;
		tempLabel.setLayoutData(gd);
		conversion.setControl(aFormula);


		stack.pack();
		stack.setSelection(1);
//		aList.setEnabled(true);

		weight.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent event) {
				// TODO Auto-generated method stub
				try {
					if ((event.character == '\u0008') || (event.character == '\u007F') || event.character == '.') {
						// allow
					} else {
						if ("".equals(event.text)){

						} else {
							Double.parseDouble(event.text);
						}
					}
				} catch (Exception e2) {
					System.out.println("error");
					event.doit = false;
				}
			}
		});
		weight.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					double a = Double.parseDouble(mkd.getText());
					double b = Double.parseDouble(weight.getText());
					double c = Double.parseDouble(concentration.getText());
					double d = a * b / c;
					String txt = sigOrig;
					txt = txt.replace("_", ""+d);
					sig.setText(txt);
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		weight.setFont(formulaFont);
		return formula;
	}

	private Composite createTop(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		MigLayout layout = new MigLayout();
//		layout.marginHeight = 0;
//		layout.marginWidth = 0;
		top.setLayout(layout);
		// top banner
//		Composite banner = new Composite(top, SWT.NONE);
//		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false));
//		layout = new GridLayout();
//		layout.marginHeight = 1;
//		layout.marginWidth = 1;
//		layout.numColumns = 10;
//		banner.setLayout(new MigLayout());
//		banner.setLayoutData("");

		// setup bold font
		boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
		boldFont = new Font(parent.getDisplay(), "Arial", 14, SWT.BOLD | SWT.ITALIC);
		tableFont = new Font(parent.getDisplay(), "Arial", 12, SWT.BOLD | SWT.ITALIC);
		formulaFont = new Font(parent.getDisplay(), "Arial", 10, SWT.BOLD | SWT.ITALIC);
//		parent.getShell().setSize(1200, 600);
//		GridData gd = null;

		Label l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_PROJECTL));
		l.setFont(boldFont);
		l.setForeground(labelColor);
		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_PROJ));
		l.setFont(boldFont);
//		gd = new GridData();
//		gd.horizontalSpan = 1;
//		gd.grabExcessHorizontalSpace = true;
//		l.setLayoutData(gd);
		l.setBackground(greyColor);
		l.setLayoutData("span 3, w 300:450:650");

		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_RX));
		l.setFont(boldFont);
		l.setForeground(labelColor);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalIndent = 50;
//		l.setLayoutData(gd);
		rx = new Text(top, SWT.RIGHT);
		rx.setFont(boldFont);
		rx.setLayoutData("w :130:");

		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_DATE));
		l.setForeground(labelColor);
		l.setFont(boldFont);
		date = new Text(top, SWT.NONE);
		date.setFont(boldFont);
		date.setLayoutData("w :130:");

		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_BIN));
		l.setFont(boldFont);
		l.setForeground(labelColor);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalIndent = 50;
//		l.setLayoutData(gd);
		bin = new Text(top, SWT.RIGHT);
		bin.setFont(boldFont);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.widthHint = 50;
//		bin.setLayoutData(gd);
		bin.setLayoutData("wrap");

//		banner = new Composite(top, SWT.NONE);
//		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false));
//		layout = new GridLayout();
//		layout.marginHeight = 1;
//		layout.marginWidth = 1;
//		layout.numColumns = 6;
//		banner.setLayout(new MigLayout());
		
		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_ID));
		l.setFont(boldFont);
		l.setForeground(labelColor);
		id = new Text(top, SWT.NONE);
		id.setFont(boldFont);
		id.setLayoutData("w :100:");

		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_NAME));
		l.setFont(boldFont);
		l.setForeground(labelColor);
		name = new Text(top, SWT.NONE);
		name.setFont(boldFont);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.grabExcessHorizontalSpace = true;
//		gd.widthHint = 550;

//		gd.minimumWidth = 100;
//		name.setLayoutData(gd);
		name.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
//				pickDrug();
			}
		});
		name.setLayoutData("span 5, w :600:");

		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_MITTE));
		l.setFont(boldFont);
		l.setForeground(labelColor);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalIndent = 30;
//		l.setLayoutData(gd);
		mitte = new Text(top, SWT.RIGHT);
		mitte.setFont(boldFont);
		mitte.setLayoutData("w :50:, wrap");
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.widthHint = 50;

//		banner = new Composite(top, SWT.NONE);
//		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false));
//		layout = new GridLayout();
//		layout.marginHeight = 1;
//		layout.marginWidth = 1;
//		layout.numColumns = 3;
//		banner.setLayout(new MigLayout());

		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_DRUG));
		l.setForeground(labelColor);
		l.setFont(boldFont);
		drug = new Combo(top, SWT.HORIZONTAL | SWT.READ_ONLY);
		List <String>allItems = new ArrayList<String>();
		allItems.add("");
		for (DosageListItem dli : MasterModel.getInstance().getDosageList()){
			if (!RxUtil.isEmpty(dli.getDosage())){
				allItems.add(dli.getDosage());
			}
		}
		String items[] = (String[]) allItems.toArray(new String[allItems.size()]);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 1;
//		gd.grabExcessHorizontalSpace = true;
//		gd.minimumWidth = 200;
		drug.setItems(items);
		drug.setFont(boldFont);
		drug.setVisibleItemCount(20);
//		drug.setLayoutData(gd);
		drug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				updateDrugSelection();

			}
		});
		drug.setLayoutData("span 7");
		
		Button selectDrug = new Button(top, SWT.NONE);
		selectDrug.setText("Fi&lter..");
		selectDrug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				SelectDrugDialog sdd = new SelectDrugDialog(getSite().getShell(), drug.getText());
				if (sdd.open()==Window.OK){
					String sel = sdd.getSelectedDrug();
					drug.setText(sel);
					updateDrugSelection();
				}
			}
		});
		selectDrug.setLayoutData("wrap");
		
//		banner = new Composite(top, SWT.NONE);
//		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false));
//		layout = new GridLayout();
//		layout.marginHeight = 5;
//		layout.marginWidth = 10;
//		layout.numColumns = 2;
//		banner.setLayout(new MigLayout());
		
		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_SIG));
		l.setFont(boldFont);
		l.setForeground(labelColor);
		sig = new Text(top, SWT.NONE);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 1;
//		gd.widthHint = 800;
		sig.setFont(boldFont);
//		sig.setLayoutData(gd);
		sig.setLayoutData("span 10, w :950:, wrap");
		
		l = new Label(top, SWT.WRAP);
		l.setText(store.getString(PreferenceConstants.P_CITY));
		l.setFont(boldFont);
		l.setForeground(labelColor);
		city = new Text(top, SWT.NONE);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 1;
//		gd.widthHint = 300;
		city.setFont(boldFont);
		city.setLayoutData("span 3, w :450:");

		Button selectCity = new Button(top, SWT.NONE);
		selectCity.setText("Fi&lter..");
		selectCity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				SelectLookupDialog sdd = new SelectLookupDialog(getSite().getShell(), city.getText(), XLSConstants.CITY_SHEET);
				if (sdd.open()==Window.OK){
					String sel = sdd.getselected();
					city.setText(sel);
				}
			}
		});
		selectCity.setLayoutData("wrap");
		
		return top;
	}

	protected Composite createAllButtonsGroup(Composite buttonsAndFormula) {
		Composite allButtonsGroup = new Composite(buttonsAndFormula, SWT.NONE);
		GridLayout layout = new GridLayout();
		GridData gd = null;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 2;
		allButtonsGroup.setLayout(layout);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalAlignment = GridData.BEGINNING;
		allButtonsGroup.setLayoutData(gd);

		c1Image = ImageDescriptor.createFromURL(getClass().getResource("/images/blank100x100.bmp")).createImage();

		Group freq = new Group(allButtonsGroup, SWT.NONE);
		freq.setText("Dosage");
		freq.setFont(formulaFont);
		freq.setForeground(labelColor);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		freq.setLayoutData(gd);
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 3;
		freq.setLayout(layout);

		c1 = new Button(freq, SWT.BOTTOM);
		addButtonDefaultImages(c1, IconPosition.Dosage1);
		c1.setText("&1");
		c1.pack();

		c2 = new Button(freq, SWT.BOTTOM);
		addButtonDefaultImages(c2, IconPosition.Dosage2);
		c2.setText("&2");
		c2.pack();

		c3 = new Button(freq, SWT.BOTTOM);
		addButtonDefaultImages(c3, IconPosition.Dosage3);
		c3.setText("&3");
		c3.pack();

		Label a1 = new Label(freq, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("(alt-1)");
		a1.setLayoutData(gd);
		a1.pack();

		a1 = new Label(freq, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("(alt-2)");
		a1.setLayoutData(gd);
		a1.pack();

		a1 = new Label(freq, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("(alt-3)");
		a1.setLayoutData(gd);
		a1.pack();

		//---------------------------------------------
		Group other = new Group(allButtonsGroup, SWT.NONE);
		other.setText("Other");
		other.setForeground(labelColor);
		other.setFont(formulaFont);
		gd = new GridData();
		gd.verticalSpan = 2;
		other.setLayoutData(gd);
		other.setLayout(new GridLayout());

		co1 = new Button(other, SWT.BOTTOM);
		addButtonDefaultImages(co1, IconPosition.Other8);
		co1.setText("&8");
		co1.pack();

		Label ao1 = new Label(other, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		ao1.setText("food (alt-8)");
		ao1.setLayoutData(gd);
		ao1.pack();

		co2 = new Button(other, SWT.BOTTOM);
		addButtonDefaultImages(co2, IconPosition.Other9);
		co2.setText("&9");
		co2.pack();

		a1 = new Label(other, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("child (alt-9)");
		a1.setLayoutData(gd);
		a1.pack();

		//---------------------------------------------
		Group when = new Group(allButtonsGroup, SWT.NONE);
		when.setText("Frequency");
		when.setFont(formulaFont);
		when.setForeground(labelColor);
		when.setLayoutData(new GridData());
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 4;
		when.setLayout(layout);

		w1 = new Button(when, SWT.BOTTOM);
		addButtonDefaultImages(w1, IconPosition.Frequency4);
		w1.setText("&4");
		w1.pack();

		w2 = new Button(when, SWT.BOTTOM);
		addButtonDefaultImages(w2, IconPosition.Frequency5);
		w2.setText("       &5");
		w2.pack();

		w3 = new Button(when, SWT.BOTTOM);
		addButtonDefaultImages(w3, IconPosition.Frequency6);
		w3.setText("&6");
		w3.pack();

		w4 = new Button(when, SWT.BOTTOM);
		addButtonDefaultImages(w4, IconPosition.Frequency7);
		w4.setText("&7");
		w4.pack();

		a1 = new Label(when, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("morning (alt-4)");
		a1.setLayoutData(gd);
		a1.pack();

		a1 = new Label(when, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("afternoon (alt-5)");
		a1.setLayoutData(gd);
		a1.pack();

		a1 = new Label(when, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("evening (alt-6)");
		a1.setLayoutData(gd);
		a1.pack();

		a1 = new Label(when, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.CENTER;
		a1.setText("night (alt-7)");
		a1.setLayoutData(gd);
		a1.pack();

		// message contents
		Composite processingButtons = new Composite(allButtonsGroup, SWT.NONE);
		layout = new GridLayout();
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 4;
		processingButtons.setLayout(layout);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = GridData.CENTER;
		processingButtons.setLayoutData(gd);

		Button printTrackB = new Button(processingButtons, SWT.PUSH);
		printTrackB.setText("&Print and Track");
		printTrackB.setFont(boldFont);
		printTrackB.setForeground(redColor);

		printTrackB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				String error = entryValid();
				if (error!=null){
					MessageDialog.openError(getSite().getShell(), "Script Entry Error", error);
					return;
				}
				Script s = getScriptFromParameters(true);
				ScriptPersist.getInstance().save(s);

				String response = VBSScriptForRxTrack.getInstance().callVbs("p", s);//
				if (response!=null){
					MessageDialog.openError(getSite().getShell(), "Error", response);
				}
				clearForNewScript();
				updateAndComeBack();
			}

		});

		final Button trackB = new Button(processingButtons, SWT.PUSH);
		trackB.setText("&Track");
		trackB.setFont(boldFont);
		trackB.setForeground(redColor);
		trackB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				String error = entryValid();
				if (error!=null){
					MessageDialog.openError(getSite().getShell(), "Error", error);
					return;
				}
				Script s = getScriptFromParameters(true);
				ScriptPersist.getInstance().save(s);

				clearForNewScript();
				updateAndComeBack();
			}
		});

		Button previewB = new Button(processingButtons, SWT.PUSH);
		previewB.setText("Pre&view");
		previewB.setFont(boldFont);
		previewB.setForeground(redColor);
		previewB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				Script s = getScriptFromParameters(true);
				String response = VBSScriptForRxTrack.getInstance().callVbs("e", s);//
				if (response!=null){
					MessageDialog.openError(getSite().getShell(), "Error", response);
				}
				try {
					Thread.sleep(1500);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				String temp = store.getString(PreferenceConstants.P_PWD);
				PreviewDialog pd = new PreviewDialog(getSite().getShell());
				pd.setData("title", "Script label for RX: " + rx.getText() + " [" + s.getPatient().getId() + " " + s.getPatient().getName() + "]");
				pd.setData("image", temp + "\\"+ s.getPatient().getId() + "_" + rx.getText() +".bmp");
				pd.open();
			}
		});

		Button clearB = new Button(processingButtons, SWT.PUSH);
		clearB.setText("&Reset");
		clearB.setFont(boldFont);
		clearB.setForeground(redColor);
		clearB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				clearForNewScript();
			}
		});

		return allButtonsGroup;
		
	}

	protected void pickDrug(){
		SelectDrugDialog sdd = new SelectDrugDialog(getSite().getShell(), drug.getText());
		if (sdd.open()==Window.OK){
			String sel = sdd.getSelectedDrug();
			drug.setText(sel);
			updateDrugSelection();
		}
	}

	protected void updateDrugSelection() {
		for (DosageListItem dli : MasterModel.getInstance().getDosageList()){
			if (drug.getText().equalsIgnoreCase(dli.getDosage()) ){
				flags = dli.getFlags();
				mitte.setText(""+dli.getMitte());
				bin.setText(dli.getBin());
				sig.setText(dli.getSig());
				sigOrig = dli.getSig();
				pictures = dli.getPictures();
				Picture picture = new Picture();
				picture.setUserSpecified(pictures);
				updateButtonPictures(picture);
				clearConversionTable();
				clearLookupTable();
				formula.setVisible(false);
				if (dli.getConversion().getRangeHeaderRows()!=null){
					updateLookupTable(dli.getConversion().getRangeHeaderRows(), dli.getConversion().getRangeRows());
					stack.setSelection(0);
					formula.setVisible(true);
				} else if (dli.getConversion().getConversionrate()!=-1){
					Integer convRatio = dli.getConversion().getConversionrate();
					updateConversionTable(convRatio);
					concentration.setText(convRatio.toString());
					stack.setSelection(1);
					formula.setVisible(true);
				}
			}
		}
	}

	protected void updateAndComeBack(){
		IViewReference r []= getSite().getPage().getViewReferences();
		IWorkbenchPart me = null;
		for (int i=0;i<r.length;i++){
			IWorkbenchPart part = r[i].getPart(true);
			getSite().getPage().activate(part);
			if (part instanceof View){
				me = part;
			}
		}
		if (me!=null){
			getSite().getPage().activate(me);
		}
	}

	protected void clearConversionTable() {
		if (mkd==null) return;
		mkd.setText("");
		weight.setText("");

		conversion.getControl().setEnabled(false);
		conversion.getControl().setForeground(blackColor);
	}

	protected void updateConversionTable(Integer integer) {
		conversion.getControl().setEnabled(true);
		conversion.getControl().setForeground(labelColor);
	}

	protected void clearLookupTable() {
		if (lookupTable==null) return;
		lookupTable.removeAll();
		lookup.setText("Lookup");
		lookup.getControl().setEnabled(false);
		lookup.getControl().setForeground(blackColor);
	}

	protected void updateLookupTable(ConversionRangeRow header, List<ConversionRangeRow> list) {

		lookupTable.removeAll();
		lookupTable.getColumn(0).setText(header.getFrom());
		lookupTable.getColumn(1).setText(header.getTo());
		lookupTable.getColumn(2).setText("Replacement");
		lookup.setText(header.getReplacement());
		for (ConversionRangeRow crr : list){
			TableItem ti = new TableItem(lookupTable, SWT.NONE);
			ti.setText(0, crr.getFrom());
			ti.setText(1, crr.getTo());
			ti.setText(2, crr.getReplacement());
		}
		stack.pack();
		lookup.getControl().setEnabled(true);
		lookup.getControl().setForeground(labelColor);
	}

	protected void addButtonDefaultImages(final Button b, IconPosition ip){
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (b.getData("state")==null || b.getData("state").equals("off")){
					b.setData("state", "on");
					try {
						if (b.getData("on")!=null){
							b.setImage((Image)ImageDescriptor.createFromURL(getClass().getResource(b.getData("on").toString())).createImage());
						}
					} catch (Exception e2) {
						MessageDialog.openError(getSite().getShell(), "Error", "Bad or Missing image file");
					}
				} else {
					b.setData("state", "off");
					b.setImage(c1Image);
				}
			}
		});
		GridData gd = new GridData();
		gd.widthHint = 115;
		gd.heightHint = 115;
		b.setImage(c1Image);
		b.setLayoutData(gd);
		b.setData("off", "/images/blank100x100.bmp");
		b.setData("IconPosition", ip);
		buttons.add(b);
	}

	private String getTime(){
		Calendar cal = Calendar.getInstance();
		StringBuffer time = new StringBuffer();
        time.append(cal.get(Calendar.HOUR_OF_DAY)).append(":");
        time.append(cal.get(Calendar.MINUTE)).append(":").append(Calendar.SECOND);
        return time.toString();
	}

	private String getPicsAbbrev(){
		Map<IconPosition, Boolean> buttonsOnOff = new HashMap<IconPosition, Boolean>();
		for (Button b : buttons){
			buttonsOnOff.put((IconPosition)(b.getData("IconPosition")), "on".equals(b.getData("state")));
		}
        Picture picture = new Picture();
        picture.setUserSpecified(pictures);
        return picture.getNewSerializedValue(buttonsOnOff);
	}

	protected void updateButtonPictures(Picture picture){
		for (Button b : buttons){
			//register image
			b.setData("on", picture.getImage((IconPosition)(b.getData("IconPosition"))));
			String state = picture.getState((IconPosition)(b.getData("IconPosition")));
			try {
				//change image
				b.setImage((Image)ImageDescriptor.createFromURL(getClass().getResource(b.getData(state).toString())).createImage() );
			} catch (Exception e) {
				MessageDialog.openError(getSite().getShell(), "Error", "Bad or Missing image file update.");
			}
			// update states
			b.setData("state", picture.getState((IconPosition)(b.getData("IconPosition"))));
			b.redraw();
		}
	}

	protected Script getScriptFromParameters(boolean niceify){
		// Add to model
		Patient pt = new Patient();
		pt.setId(id.getText());
		pt.setName(name.getText());
		pt.setCity(city.getText());
		MasterModel.getInstance().getPatientList().add(0, pt);
		Script s = new Script();
		s.setPatient(pt);
		s.setInventoryItem(InventoryModelProvider.getInstance().getInventoryItems().get(drug.getSelectionIndex()));
		for (InventoryItem ii : InventoryModelProvider.getInstance().getInventoryItems()){
			if (ii.getDosage().equals(drug.getText())){
				s.setInventoryItem(ii);
				break;
			}
		}
		s.setSig(sig.getText());
		s.setMitte(mitte.getText());
		s.setPics(getPicsAbbrev());
		s.setDate(date.getText());
		s.setTime(getTime());
		s.setRx(rx.getText());
		
		if (niceify){
			s.niceify();
		}
		return s;
	}

	public void clearForNewScript(){
		id.setText("");
		name.setText("");
		rx.setText(""+MasterModel.getInstance().getNextRX());
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		date.setText(sdf.format(d));
		drug.setText("");
		sig.setText("");
		sigOrig = "";
		mitte.setText("");
		bin.setText("");
		city.setText("");
		flags = "";

		weight.setText("");
		mkd.setText("");

		for (Button b : buttons){
			b.setImage(c1Image); // set blank
			b.setData("state", "off");
			b.redraw();
		}
		
		stack.setSelection(0);
		clearConversionTable();
		clearLookupTable();
		formula.setVisible(false);
		id.setFocus();
	}

	public void setFocus() {
		if (id!=null)
		id.forceFocus();
	}

	/**
	 * Update patient from another view
	 * @param p
	 */
	public void updatePatient(Patient p){
		name.setText(p.getName());
		id.setText(p.getId());
		city.setText(p.getCity());
	}

	/**
	 * Update script from another view
	 */
	public void updateDosage(Script s){
		sigOrig = s.getInventoryItem().getSig();
		drug.setText(s.getInventoryItem().getDosage());
		mitte.setText(s.getMitte());
		bin.setText(s.getInventoryItem().getBin());
		Picture picture = new Picture();
		picture.setUserSpecified(s.getPics());
		updateButtonPictures(picture);
		updateDrugSelection();
		sig.setText(s.getSig());
	}
	
	public String entryValid(){
		if (flags!=null && flags.indexOf("C")>=0){
			if (city.getText().trim().length()==0){
				return "Field requires a value: \"" + store.getString(PreferenceConstants.P_CITY) + "\".";
			}
		}
		return null;
	}
}
