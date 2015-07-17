package ro.aquacola.compositelaunch;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Launch configuration and launch mode pair.
 * 
 * @author Cornel Izbasa <cizbasa@info.uvt.ro>
 *
 */
public interface ICompositeConfigMode {

	/**
	 * Retrieve the launch configuration.
	 * 
	 * @return the launch configuration
	 */
	public abstract ILaunchConfiguration getConfig();

	/** Retrieve the launch mode.
	 * 
	 * @return the launch mode
	 */
	public abstract String getMode();

	/**
	 * Set the launch mode.
	 * 
	 * @param value the launch mode
	 */
	public abstract void setMode(Object value);

	/**
	 * Retrieve the possible launch modes for the launch configuration.
	 * 
	 * @return the possible launch modes for the launch configuration
	 */
	public abstract String[] getChoices();
}