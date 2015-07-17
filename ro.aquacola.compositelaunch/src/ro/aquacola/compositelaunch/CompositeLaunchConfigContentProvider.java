package ro.aquacola.compositelaunch;

import static ro.aquacola.internal.compositelaunch.CompositeLaunchStrings.CompositeLaunch_PathSeparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The content provider for the tree viewer
 * 
 * @since 3.4.0
 */
public class CompositeLaunchConfigContentProvider implements ITreeContentProvider {

	
	public static CompositeLaunchConfigContentProvider INSTANCE = new CompositeLaunchConfigContentProvider();
	
	private CompositeLaunchConfigContentProvider() {
		
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();

		if (parentElement instanceof ILaunchConfigurationType) {
			try {
				return lm.getLaunchConfigurations((ILaunchConfigurationType) parentElement);
			} catch (CoreException e) {
				CompositeLaunchActivator.logErrorMessage(e);
			}
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ILaunchConfiguration) {
			try {
				return ((ILaunchConfiguration) element).getType();
			} catch (CoreException e) {
				CompositeLaunchActivator.logErrorMessage(e);
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ILaunchConfigurationType;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();

		return lm.getLaunchConfigurationTypes();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * Retrieve a launch configuration based on its URI-like string form.
	 * 
	 * @param configURI the string form of the launch configuration
	 * @return the launch configuration
	 */
	public ILaunchConfiguration dszTreePath(String configURI) {
		Object[] items = getElements(null);

		String[] split = configURI.split(CompositeLaunch_PathSeparator);
		for (Object item : items) {
			assert(item instanceof ILaunchConfigurationType);			
			String typeId = ((ILaunchConfigurationType) item).getIdentifier();
			if (split[0].equals(typeId)) {
				return dszLaunchConfig(split[1], getChildren(item));
			}
		}
		return null;
	}
	
	/**
	 * Retrieve the launch configuration from its sibling configurations.
	 * 
	 * @param configName the launch configuration name
	 * @param objects the sibling configurations
	 * @return the launch configuration with the given name
	 */
	private ILaunchConfiguration dszLaunchConfig(String configName, Object[] objects) {
		for (Object object : objects) {
			ILaunchConfiguration cfg = (ILaunchConfiguration) object;
			if (cfg.getName().equals(configName)) {
				return (ILaunchConfiguration)object;
			}
		}
		return null;
	}
	
	/**
	 * Join strings by the given glue string.
	 * 
	 * @param collection the strings
	 * @param connection the glue string
	 * @return the concatenated string
	 */
	private String joinStrings(Collection<String> collection, String connection) {
		StringBuilder result = new StringBuilder();
		Iterator<String> it = collection.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			if (it.hasNext()) {
				result.append(connection); 
			}
		}
		return result.toString();	
	}
	
	/**
	 * String-form of the given tree path.
	 * 
	 * @param segments the launch config type and the launch config
	 * @return {LaunchConfigTypeId}/{LaunchConfigName}
	 */
	private String szTreePath(Object[] segments) {
		List<String> ids = new ArrayList<String>();
		ids.add(((ILaunchConfigurationType)segments[0]).getIdentifier());
		ids.add(((ILaunchConfiguration)segments[1]).getName());		
		
		return joinStrings(ids, CompositeLaunch_PathSeparator);
	}
	
	/**
	 * The path as an array of objects to the given element.
	 * 
	 * Should contain just two elements: the launch configuration type and the launch configuration itself.
	 * 
	 * @param element the launch configuration
	 * @return the parent launch configuration type and the launch configuration as an array.
	 */
	private Object[] treePath(Object element) {
		List<Object> itemList = new ArrayList<Object>();
		for (Object current = element; current != null; current = getParent(current)) {
			itemList.add(0, current);
		}

		if (2 != itemList.size()) {
			return null;
		}
		return itemList.toArray();
	}
	
	/**
	 * String form of a given launch configuration.
	 * 
	 * @param linkedConfig the launch configuration
	 * @return {LaunchConfigType}/{LaunchConfigName}
	 */
	public String szOneConfig(ILaunchConfiguration linkedConfig) {
		return szTreePath(treePath(linkedConfig));
	}
	
}