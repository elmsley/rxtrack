package com.rxtrack;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	public static final String CMD_PREFIX = "RxTrack.";
    public static final String CMD_OPEN = CMD_PREFIX + "open";
    public static final String CMD_OPEN_MESSAGE = CMD_PREFIX + "openMessage";
    public static final String CMD_OPEN_INV = CMD_PREFIX + "openInventory";
    public static final String CMD_OPEN_PAT = CMD_PREFIX + "openPatients";
    public static final String CMD_OPEN_SCRIPTS = CMD_PREFIX + "openScripts";
    public static final String CMD_EXPORT_INV = CMD_PREFIX + "exportInventory";
    public static final String CMD_EXPORT_SCRIPTS = CMD_PREFIX + "exportScripts";
    
}
