/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Cornel Izbasa <cizbasa@info.uvt.ro> - Adapted to Composite launch plugin
 *******************************************************************************/
package ro.aquacola.compositelaunch.internal.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.activities.WorkbenchActivityHelper;

import ro.aquacola.compositelaunch.ui.CompositeLaunchUIActivator;


public abstract class AbstractLaunchFilter extends ViewerFilter {
	private String fAttribute;
	
	protected void setAttribute(String attribute) {
		fAttribute = attribute;
	}
	
	protected abstract String getAttribute(ILaunchConfigurationType type);
	
	/**
	 * Constructs a filter that hides configurations from a specific
	 * category.
	 * 
	 * @param groupExtension
	 */
	public AbstractLaunchFilter(String attribute) {
		setAttribute(attribute);
	}

	/**
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
			ILaunchConfigurationType type = null;
			ILaunchConfiguration config = null;
			if (parentElement instanceof ILaunchConfigurationType) {
				type = (ILaunchConfigurationType)parentElement;
			}
			if (element instanceof ILaunchConfigurationType) {
				type = (ILaunchConfigurationType)element;
			}
			if (element instanceof ILaunchConfiguration) {
				config = (ILaunchConfiguration)element;
				try {
					type = config.getType();
				} 
				catch (CoreException e) {
					CompositeLaunchUIActivator.logErrorMessage(e);					
				}
			}
			boolean priv = false;
			if (config != null) {
				try {
					priv = config.getAttribute(IDebugUIConstants.ATTR_PRIVATE, false);
				} catch (CoreException e) {
					CompositeLaunchUIActivator.logErrorMessage(e);					
				}
			} else if (type != null) {
				priv = !type.isPublic();
			}
			if (type != null) {
				return !priv && !equalAttribute(getAttribute(type), fAttribute) && !WorkbenchActivityHelper.filterItem(new CompositeLaunchConfigurationTypeContribution(type));
			}
			return true;
	}
	
	/**
	 * Returns whether the given categories are equal.
	 * 
	 * @param c1 category identifier or <code>null</code>
	 * @param c2 category identifier or <code>null</code>
	 * @return boolean
	 */
	private boolean equalAttribute(String c1, String c2) {
		if (c1 == null || c2 == null) {
			return c1 == c2;
		}
		return c1.equals(c2);
	} 
}
