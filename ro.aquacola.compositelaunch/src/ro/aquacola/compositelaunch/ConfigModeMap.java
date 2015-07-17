package ro.aquacola.compositelaunch;

import static ro.aquacola.internal.compositelaunch.CompositeLaunchStrings.CompositeLaunch_Attribute;
import static ro.aquacola.internal.compositelaunch.CompositeLaunchStrings.EMPTY_STRING;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

/**
 * Class containing most of the logic for serialization and management of 
 * launch configuration and associated modes in a composite launch configuration.
 * 
 * @author Cornel Izbasa <cizbasa@info.uvt.ro>
 *
 */
public class ConfigModeMap extends LinkedHashMap<String, String> {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -5561107486440918931L;

	public static ConfigModeMap INSTANCE = new ConfigModeMap(); 
				
	private ConfigModeMap() {
		
	}

	/**
	 * Retrieve the current set of launch configurations.
	 * 
	 * @return the current set of launch configurations
	 */
	public Set<ILaunchConfiguration> configs() {
		Set<ILaunchConfiguration> configs = new LinkedHashSet<ILaunchConfiguration>();
		for(String key: keySet()) {
			configs.add(configForName(key));
		};
		return configs;
	}
	
	/**
	 * Retrieve the current entries as an array of {@link CompositeConfigMode}s.
	 * @return the array of configuration / mode pairs.
	 * 
	 * @see CompositeConfigMode
	 */
	public ICompositeConfigMode[] configsModes() {
		Set<String> keySet = keySet();
		ICompositeConfigMode[] configs = new ICompositeConfigMode[keySet.size()];
		int i = 0;
		for(String key: keySet) {
			configs[i++] = new CompositeConfigMode(configForName(key), get(key));
		};
		return configs;		
	}

	/**
	 * Retrieve a launch configuration based on its URI-like form.
	 * @param key the URI-like form of the launch configuration
	 * @return the launch configuration
	 */
	public ILaunchConfiguration configForName(String key) {
		return CompositeLaunchConfigContentProvider.INSTANCE.dszTreePath(key);
	}
	
	/**
	 * Retrieve the name of the specified launch configuration.
	 * 
	 * @param config the specified launch configuration
	 * @return the name of the configuration
	 */
	String configName(ILaunchConfiguration config) {
		return CompositeLaunchConfigContentProvider.INSTANCE.szOneConfig(config);
	}
	
	/**
	 * Add a launch configuration with the given mode.
	 * 
	 * @param config the launch configuration
	 * @param mode the launch mode
	 */
	public void put(ILaunchConfiguration config, String mode) {
		put(configName(config), mode);
	}
	
	@SuppressWarnings("deprecation")
	private String defaultMode(ILaunchConfiguration config) {
		try {
			return config.getType().getSupportedModes().iterator().next();
		} catch (CoreException e) {
			CompositeLaunchActivator.logErrorMessage(e);
		}
		return null;
	}
	
	/**
	 * Update the current entries based on the selected ones from {@link configs},
	 * preserving the currently selected modes for persistent configurations.
	 * 
	 * @param configs the given confiurations
	 */
	public void putConfigs(Object[] configs) {
		Map<String, String> aux = new LinkedHashMap<String, String>();
		aux.putAll(this);
				
		clear();
		
		for(Object check: configs) {
			if (check instanceof ILaunchConfiguration) {
				ILaunchConfiguration cfg = (ILaunchConfiguration) check;
				String saved = aux.get(configName(cfg));
				if (saved != null) {
					// Preserve existing mode.
					put((ILaunchConfiguration)check, saved);
				} else { 
					put((ILaunchConfiguration)check);
				}
			}
		}		
	}
	
	/**
	 * Add the specified launch configuration to this composite 
	 * launch configuration with its default launch mode.
	 * 
	 * @param config the specified launch configuration
	 */
	public void put(ILaunchConfiguration config) {
		put(config, defaultMode(config));
	}
	
	/**
	 * Retrieve current entries (launch configuration and mode pairs).
	 * 
	 * @return the current entries and associated modes
	 */
	private String[][] getEntries() {
		Set<String> keySet = keySet();
		String[][] result = new String[keySet.size()][2];
		List<String> keyList = new ArrayList<String>(keySet);
		Collections.sort(keyList);
		int i = 0;
		for(String key: keyList) {
			result[i][0] = key;
			result[i++][1] = get(key);
		}
		
		return result;
	}

	/**
	 * Populate entries from the given array.
	 * 
	 * @param entries the given entries
	 */
	private void setEntries(String[][] entries) {
		clear();

		for(int i = 0; i < entries.length; i++) {
			if (configForName(entries[i][0]) != null && entries[i][1] != null) {
				put(entries[i][0], entries[i][1]);
			};
		}		
	}
	
	/**
	 * Create serialization form for the current entries.
	 * 
	 * @return the serialized form
	 */
	public String save() {		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ObjectOutput s = null;
		try {
			s = new ObjectOutputStream(bs);
			s.writeObject(getEntries());
			s.flush();

		} catch (IOException e) {
			CompositeLaunchActivator.logErrorMessage(e);
			throw new RuntimeException(e);
		}
		return javax.xml.bind.DatatypeConverter.printBase64Binary(bs.toByteArray());
	}
	
	/**
	 * Load entries from the given string.
	 * 
	 * @param serialized the given string.
	 */
	public void load(String serialized) {
		clear();
		
		if (serialized == null || serialized.isEmpty()) {
			return;
		}
		byte[] parseBase64Binary = javax.xml.bind.DatatypeConverter.parseBase64Binary(serialized);
		ByteArrayInputStream bs = new ByteArrayInputStream(parseBase64Binary);
		ObjectInput s = null;
		try {
			s = new ObjectInputStream(bs);
			String[][] entries = (String[][])s.readObject();
			setEntries(entries);
		} catch (ClassNotFoundException e) {
			CompositeLaunchActivator.logErrorMessage(e);
			throw new RuntimeException(e);			
		} catch (IOException e) {
			CompositeLaunchActivator.logErrorMessage(e);
			throw new RuntimeException(e);
		} // Not using multicatch for compatibility with Java 6.
	}
	
	/**
	 * Load entries from the given composite launch configuration.
	 * 
	 * @param composite the given composite launch configuration
	 */
	public void load(ILaunchConfiguration composite) {
		String attribute = null;
		try {
			attribute = composite.getAttribute(CompositeLaunch_Attribute, EMPTY_STRING);
		} catch (CoreException e) {
			CompositeLaunchActivator.logErrorMessage(e);
		}		
		ConfigModeMap.INSTANCE.load(attribute);
	}
	
	/**
	 * Save the currently selected launch configurations to the given composite R/W launch config.
	 * 
	 * @param composite the composite R/W launch config
	 * @param checked the selected launch configurations
	 */
	public void save(ILaunchConfigurationWorkingCopy composite, Object[] checked) {
		putConfigs(checked);		
		save(composite);
	}

	/**
	 * Save entries to a given R/W launch configuration.
	 * @param composite the given R/W launch configuration
	 */
	public void save(ILaunchConfigurationWorkingCopy composite) {
		composite.setAttribute(CompositeLaunch_Attribute, save());
	}
}
