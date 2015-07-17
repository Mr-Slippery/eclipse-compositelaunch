package ro.aquacola.compositelaunch.internal.ui;

import org.eclipse.debug.core.ILaunchConfigurationType;

public class CompositeLaunchIdFilter extends AbstractLaunchFilter {

	public CompositeLaunchIdFilter(String attribute) {
		super(attribute);
	}

	@Override
	protected String getAttribute(ILaunchConfigurationType type) {
		return type.getIdentifier();
	}
}
