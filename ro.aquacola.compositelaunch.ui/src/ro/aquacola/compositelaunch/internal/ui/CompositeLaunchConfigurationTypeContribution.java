package ro.aquacola.compositelaunch.internal.ui;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.ui.IPluginContribution;

/**
 * A plug-in contribution (UI element) which contains a launch configuration
 * type (Core element). Plug-in contributions are passed to the workbench
 * activity support to filter elements from the UI.
 */
public class CompositeLaunchConfigurationTypeContribution implements IPluginContribution {
	
	protected ILaunchConfigurationType type;
	
	/**
	 * Creates a new plug-in contribution for the given type
	 * 
	 * @param type the launch configuration type
	 */
	public CompositeLaunchConfigurationTypeContribution(ILaunchConfigurationType type) {
		this.type= type;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPluginContribution#getLocalId()
	 */
	@Override
	public String getLocalId() {
		return type.getIdentifier();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPluginContribution#getPluginId()
	 */
	@Override
	public String getPluginId() {
		return type.getPluginIdentifier();
	}		
}

