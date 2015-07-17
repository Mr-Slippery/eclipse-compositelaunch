/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software Systems - Mikhail Khodjaiants - Bug 114664
 *     Wind River Systems - Pawel Piech - Added Modules view (bug 211158)
 *     Cornel Izbasa <cizbasa@info.uvt.ro> - Adapted to Composite launch plugin
 *******************************************************************************/
package ro.aquacola.compositelaunch.internal.ui;

public interface IInternalDebugUIConstants {
    
    /**
     * Identifier of the external tool builder launch category. Defined here since
     * external tools is actually a dependent plug-in.
     * 
     * @since 3.4
     */
    public static final String ID_EXTERNAL_TOOL_BUILDER_LAUNCH_CATEGORY = "org.eclipse.ui.externaltools.builder";  //$NON-NLS-1$
    
}
