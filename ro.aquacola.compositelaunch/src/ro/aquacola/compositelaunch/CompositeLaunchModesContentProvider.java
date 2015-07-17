package ro.aquacola.compositelaunch;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The content provider for the tree viewer
 */
public class CompositeLaunchModesContentProvider implements IStructuredContentProvider {

	public static CompositeLaunchModesContentProvider INSTANCE = new CompositeLaunchModesContentProvider();
	
	private CompositeLaunchModesContentProvider() {
		
	}
	
	private Object[] fResult = null;
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (!(newInput instanceof ConfigModeMap)) {
			return;
		}
		ConfigModeMap map = (ConfigModeMap)newInput;
		
		Set<Entry<String, String>> entrySet = map.entrySet();
		Object[] result = new ICompositeConfigMode[entrySet.size()];
		
		int i = 0;
		for(Entry<String, String> entry: entrySet) {
			result[i++] = new CompositeConfigMode(map.configForName(entry.getKey()), entry.getValue());			
		}
		fResult = result;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return fResult;
	}
	
	/**
	 * Store the currently selected launch modes to the given map.
	 * @param map the given {@link ConfigModeMap}
	 */
	public void szModes(ConfigModeMap map) {
		for(Object element: fResult) {
			if (element instanceof ICompositeConfigMode) {
				ICompositeConfigMode cm = (ICompositeConfigMode) element;
				ILaunchConfiguration config = cm.getConfig();
				String szOneConfig = CompositeLaunchConfigContentProvider.INSTANCE.szOneConfig(config);				
				if(map.containsKey(szOneConfig)) {
					map.put(cm.getConfig(), cm.getMode());					
				}
			}
		}
	}

	@Override
	public void dispose() {
	}
}