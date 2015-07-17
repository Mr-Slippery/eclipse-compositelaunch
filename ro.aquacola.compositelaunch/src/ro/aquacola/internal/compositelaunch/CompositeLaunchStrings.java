package ro.aquacola.internal.compositelaunch;

import org.eclipse.osgi.util.NLS;

public class CompositeLaunchStrings extends NLS {
	private static final String BUNDLE_NAME = "ro.aquacola.internal.compositelaunch.CompositeLaunchStrings"; //$NON-NLS-1$
	
	public static String CompositeLaunch_Attribute;
	public static String CompositeLaunchModes_Attribute;
	public static String CompositeLaunch_PathSeparator;
	public static String CompositeLaunch_Separator;
	public static String CompositeLaunch_InternalMessage;
	public static String EMPTY_STRING;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CompositeLaunchStrings.class);
	}

	private CompositeLaunchStrings() {
	}
}
