package ro.aquacola.compositelaunch;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.osgi.framework.BundleContext;

import ro.aquacola.internal.compositelaunch.CompositeLaunchStrings;

/**
 * The activator class controls the plug-in life cycle
 */
public class CompositeLaunchActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ro.aquacola.compositelaunch"; //$NON-NLS-1$

	// The shared instance
	private static CompositeLaunchActivator plugin;
	
	/**
	 * The constructor
	 */
	public CompositeLaunchActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CompositeLaunchActivator getDefault() {
		return plugin;
	}

	
	/**
	 * Logs an internal error with the specified message.
	 * 
	 * @param message the error message to log
	 */
	public static void logErrorMessage(String message) {
		log(newErrorStatus(CompositeLaunchStrings.CompositeLaunch_InternalMessage + message, null));
	}

	/**
	 * Returns a new error status for this plug-in with the given message
	 * @param message the message to be included in the status
	 * @param exception the exception to be included in the status or <code>null</code> if none
	 * @return a new error status
	 */
	public static IStatus newErrorStatus(String message, Throwable exception) {
		return new Status(IStatus.ERROR, CompositeLaunchActivator.PLUGIN_ID, IDebugUIConstants.INTERNAL_ERROR, message, exception);
	}
	
	public static void logErrorMessage(Throwable e) {
		logErrorMessage(e.getMessage());
	}
	
	/**
	 * Logs the specified status with this plug-in's log.
	 * 
	 * @param status status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}	
}
