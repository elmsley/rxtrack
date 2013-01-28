package com.rxtrack.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
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

public class TextLabelsPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public TextLabelsPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("You can change the text labels of any of the following:");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_PROJECTL, "Project:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_ID, "ID#:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_NAME, "Name#:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_RX, "RX#:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_DATE, "Date:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_DRUG, "DRUG:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_MITTE, "Mitte:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_BIN, "BIN:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_SIG, "SIG:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_CITY, "City:", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}