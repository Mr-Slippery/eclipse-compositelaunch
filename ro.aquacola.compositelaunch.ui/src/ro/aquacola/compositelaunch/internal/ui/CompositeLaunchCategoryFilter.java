package ro.aquacola.compositelaunch.internal.ui;

import org.eclipse.debug.core.ILaunchConfigurationType;


public class CompositeLaunchCategoryFilter extends AbstractLaunchFilter {

	public CompositeLaunchCategoryFilter(String attribute) {
		super(attribute);
	}

	@Override
	protected String getAttribute(ILaunchConfigurationType type) {
		return type.getCategory();
	}
}
