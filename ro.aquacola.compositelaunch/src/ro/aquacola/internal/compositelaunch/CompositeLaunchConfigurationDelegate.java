package ro.aquacola.internal.compositelaunch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import ro.aquacola.compositelaunch.ConfigModeMap;
import ro.aquacola.compositelaunch.ICompositeConfigMode;

public class CompositeLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		ConfigModeMap.INSTANCE.load(configuration);
		
		ICompositeConfigMode[] linkedConfigs = ConfigModeMap.INSTANCE.configsModes();
		for(ICompositeConfigMode cm: linkedConfigs) {
			ILaunchConfiguration config = cm.getConfig();
			String configMode = cm.getMode();
			
			@SuppressWarnings("deprecation")
			ILaunchConfigurationDelegate delegate = config.getType().getDelegate(configMode);
			delegate.launch(config, configMode, launch, monitor);
		}
	}

}
