package com.rxtrack.ui.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rxtrack.Activator;


/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class RxTrackPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public RxTrackPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Modify these preferences for each project you run.  Files and labels will reflect the information entered.");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
//		addField(
//			new BooleanFieldEditor(
//				PreferenceConstants.P_BOOLEAN,
//				"&An example of a boolean preference",
//				getFieldEditorParent()));
//
//		addField(new RadioGroupFieldEditor(
//				PreferenceConstants.P_CHOICE,
//			"An example of a multiple-choice preference",
//			1,
//			new String[][] { { "&Choice 1", "choice1" }, {
//				"C&hoice 2", "choice2" }
//		}, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PROJ, "Project:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_RXSTART, "Rx Staring Number:", getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_LABELFILE, "Label Template File:", getFieldEditorParent()));
		FileFieldEditor ffe = new FileFieldEditor(PreferenceConstants.P_DOSAGEFILE, "Dosage File:", getFieldEditorParent());
		ffe.setFileExtensions(new String[]{"*.xls", "*.xlsx"});
		addField(ffe);
		addField(new DirectoryFieldEditor(PreferenceConstants.P_PWD, "Working Directory:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_LOGFILE, "Log file (prefix only):", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		return super.performOk();
	}
}