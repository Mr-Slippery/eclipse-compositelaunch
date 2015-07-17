package ro.aquacola.compositelaunch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import ro.aquacola.internal.compositelaunch.CompositeLaunchStrings;
import static ro.aquacola.internal.compositelaunch.CompositeLaunchStrings.*;

public class CompositeLaunchModesLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ICompositeConfigMode p = (ICompositeConfigMode) element;
		String result = EMPTY_STRING;
		switch(columnIndex){
		case 0:
			ILaunchConfiguration config = p.getConfig();
			try {
				result = config.getType().getName() + 
						 CompositeLaunchStrings.CompositeLaunch_PathSeparator + 
						 config.getName();
			} catch (CoreException e) {
				CompositeLaunchActivator.logErrorMessage(e);
			}
			break;
		case 1:
			result = p.getMode();
			break;
		default:
			//should not reach here
			result = EMPTY_STRING;
		}
		return result;
	}
}
