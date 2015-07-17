package ro.aquacola.compositelaunch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public class CompositeConfigMode implements ICompositeConfigMode {
	private ILaunchConfiguration fConfig;
	private String fMode;

	public CompositeConfigMode(ILaunchConfiguration config, String mode) {
		fConfig = config;
		fMode = mode;
	}
	
	/* (non-Javadoc)
	 * @see ro.aquacola.compositelaunch.ICompositeConfigMode#getConfig()
	 */
	@Override
	public ILaunchConfiguration getConfig() {
		return fConfig;
	}
	
	/* (non-Javadoc)
	 * @see ro.aquacola.compositelaunch.ICompositeConfigMode#getMode()
	 */
	@Override
	public String getMode() {
		return fMode;
	}

	/* (non-Javadoc)
	 * @see ro.aquacola.compositelaunch.ICompositeConfigMode#setMode(java.lang.Object)
	 */
	@Override
	public void setMode(Object value) {
		fMode = (String) value;
	}
	
	/* (non-Javadoc)
	 * @see ro.aquacola.compositelaunch.ICompositeConfigMode#getChoices()
	 */
	@Override
	public String[] getChoices() {
		String[] choices = null;
		try {
			@SuppressWarnings("deprecation")
			Object[] array = getConfig().getType().getSupportedModes().toArray();
			choices = new String[array.length];
			for(int j = 0; j < choices.length; j++) {
				choices[j] = (String) array[j];
			}
		} catch (CoreException e) {
			CompositeLaunchActivator.logErrorMessage(e);
		}
		return choices;		
	}
	
	public String toString() {
		return getConfig().getName() + ": " + getMode();
	}

}
