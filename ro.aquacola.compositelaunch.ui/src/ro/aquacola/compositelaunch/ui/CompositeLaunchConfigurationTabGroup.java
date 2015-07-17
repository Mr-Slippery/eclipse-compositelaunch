package ro.aquacola.compositelaunch.ui;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class CompositeLaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	public CompositeLaunchConfigurationTabGroup() {
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// XML-based definition doesn't seem to ensure reliable ordering,
		// so doing it here instead.
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { 
				new CompositeLaunchConfigurationTab(),
				new CompositeLaunchModesTab()
		};
		setTabs(tabs);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		super.setDefaults(config);
	}
}
