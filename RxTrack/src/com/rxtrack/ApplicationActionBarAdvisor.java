package com.rxtrack;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.rxtrack.actions.ExportInventoryAction;
import com.rxtrack.actions.ExportScriptAction;
import com.rxtrack.actions.MessagePopupAction;
import com.rxtrack.actions.OpenViewAction;
import com.rxtrack.ui.entry.View;
import com.rxtrack.ui.inventory.InventoryView;
import com.rxtrack.ui.patient.PatientsView;
import com.rxtrack.ui.script.ScriptsView;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction showHelpAction; // NEW
    private IWorkbenchAction searchHelpAction; // NEW
    private IWorkbenchAction dynamicHelpAction; // NEW
    private IWorkbenchAction newWindowAction;
    private OpenViewAction openViewAction;
    private OpenViewAction openInventoryViewAction;
    private OpenViewAction openPatientsViewAction;
    private OpenViewAction openScriptsViewAction;
    private IWorkbenchAction prefsAction;
    private Action messagePopupAction;
    private Action exportScriptAction;
    private Action exportInventoryAction;
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        showHelpAction = ActionFactory.HELP_CONTENTS.create(window); // NEW
        register(showHelpAction); // NEW

        searchHelpAction = ActionFactory.HELP_SEARCH.create(window); // NEW
        register(searchHelpAction); // NEW

        dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window); // NEW
        register(dynamicHelpAction); // NEW
        
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        
        openViewAction = new OpenViewAction(window, "&New Script Entry", View.ID, ICommandIds.CMD_OPEN, false);
        register(openViewAction);
        
        
        openInventoryViewAction = new OpenViewAction(window, "&Inventory", InventoryView.ID, ICommandIds.CMD_OPEN_INV, false);
        register(openInventoryViewAction);
        
        openPatientsViewAction = new OpenViewAction(window, "&Patients", PatientsView.ID, ICommandIds.CMD_OPEN_PAT, false);
        register(openPatientsViewAction);
        
        openScriptsViewAction = new OpenViewAction(window, "&Scripts", ScriptsView.ID, ICommandIds.CMD_OPEN_SCRIPTS, false);
        register(openScriptsViewAction);
        
        messagePopupAction = new MessagePopupAction("Reload &Dosage File", window, getActionBarConfigurer().getStatusLineManager());
        register(messagePopupAction);
        
        prefsAction = ActionFactory.PREFERENCES.create(window);
        
        exportScriptAction = new ExportScriptAction("&Export Scripts to Excel", window, getActionBarConfigurer().getStatusLineManager());
        register(exportScriptAction);
        
        exportInventoryAction = new ExportInventoryAction("&Export Inventory to Excel", window, getActionBarConfigurer().getStatusLineManager());
        register(exportInventoryAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        
        menuBar.add(fileMenu);

        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        
        menuBar.add(windowMenu);
        windowMenu.add(prefsAction);
        
        menuBar.add(helpMenu);
        
        // File
//        fileMenu.add(newWindowAction);
        fileMenu.add(new Separator());
        fileMenu.add(openViewAction);
        fileMenu.add(new Separator());
        fileMenu.add(openInventoryViewAction);
        fileMenu.add(openPatientsViewAction);
        fileMenu.add(openScriptsViewAction);
        fileMenu.add(new Separator());
        fileMenu.add(messagePopupAction);
        fileMenu.add(exportScriptAction);
        fileMenu.add(exportInventoryAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        // Help
        helpMenu.add(aboutAction);
        
        helpMenu.add(showHelpAction); // NEW
        helpMenu.add(searchHelpAction); // NEW
        helpMenu.add(dynamicHelpAction); // NEW
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        toolbar.add(openViewAction);
        toolbar.add(new Separator());
        toolbar.add(openInventoryViewAction);
        toolbar.add(openPatientsViewAction);
        toolbar.add(openScriptsViewAction);
        toolbar.add(new Separator());
        toolbar.add(exportScriptAction);
        toolbar.add(exportInventoryAction);
    }
}
