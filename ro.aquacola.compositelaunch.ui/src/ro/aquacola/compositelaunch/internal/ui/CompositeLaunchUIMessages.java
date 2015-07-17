/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package ro.aquacola.compositelaunch.internal.ui;

import org.eclipse.osgi.util.NLS;

public class CompositeLaunchUIMessages extends NLS {
	private static final String BUNDLE_NAME = "ro.aquacola.compositelaunch.internal.ui.CompositeLaunchUIMessages"; //$NON-NLS-1$
	public static String CompositeLaunchUI_LaunchConfigurations;
	public static String CompositeLaunchUI_SelectOne;
	public static String CompositeLaunchUI_SelectAll;
	public static String CompositeLaunchUI_DeselectAll;
	public static String CompositeLaunchUI_LaunchModes;
	public static String CompositeLaunchUI_InternalMessage;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CompositeLaunchUIMessages.class);
	}

	private CompositeLaunchUIMessages() {
	}
}
