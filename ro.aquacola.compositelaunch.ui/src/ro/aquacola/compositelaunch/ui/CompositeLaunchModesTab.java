package ro.aquacola.compositelaunch.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import ro.aquacola.compositelaunch.CompositeLaunchModesContentProvider;
import ro.aquacola.compositelaunch.CompositeLaunchModesLabelProvider;
import ro.aquacola.compositelaunch.ConfigModeMap;
import ro.aquacola.compositelaunch.internal.ui.CompositeLaunchUIMessages;
import ro.aquacola.compositelaunch.internal.ui.SWTFactory;

public class CompositeLaunchModesTab extends
		AbstractLaunchConfigurationTab {
	
	/**
	 * The table viewer for launch configuration and associated launch modes.
	 */
	private TableViewer fViewer = null;
	
	/**
	 * Content provider for the table of launch configurations and modes.
	 */
	private CompositeLaunchModesContentProvider fContentProvider = null;
	
	/**
	 * Validity status of this tab.
	 */
	private boolean fValid = false;
	
	/**
	 * Label provider for the table of launch configurations and modes.
	 */
	private CompositeLaunchModesLabelProvider fLabelProvider;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(getControl(), getHelpContextId());
		comp.setLayout(new GridLayout(2, true));
		comp.setFont(parent.getFont());

		createViewer(comp);

		validateTab();
	}

	/**
	 * Creates the check table viewer portion of the control
	 * 
	 * @param parent
	 *            the parent to add the check table viewer to
	 */
	protected void createViewer(Composite parent) {
		SWTFactory.createWrapLabel(parent,
				CompositeLaunchUIMessages.CompositeLaunchUI_LaunchModes, 2);
	    final Table table = new Table(parent, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(SWT.BEGINNING, SWT.FILL, true, true);
		gd.horizontalSpan = 1;
		table.setLayoutData(gd);
	    table.setLinesVisible(true);
	    
	    final String[] columnNames = new String[] { "Configuration", "Mode" };
	    
		fViewer = new TableViewer(table);

		fViewer.setUseHashlookup(true);
		fViewer.setColumnProperties(columnNames);
		
	    TableViewerColumn column = new TableViewerColumn(fViewer, SWT.NONE);	    
	    column.getColumn().setText(columnNames[0]);
	    column.getColumn().setWidth(320);
	    TableViewerColumn mode = new TableViewerColumn(fViewer, SWT.NONE);
	    mode.getColumn().setText(columnNames[1]);
	    mode.getColumn().setWidth(150);
		
	    Runnable listener = new Runnable() {
			public void run()  {
				validateTab();
				updateLaunchConfigurationDialog();
			}
	    };
	    
		EditingSupport exampleEditingSupport = new CompositeConfigModeEditingSupport(mode.getViewer(), listener);
		mode.setEditingSupport(exampleEditingSupport);			
	}

	private void validateTab() {
		setErrorMessage(null);
		setMessage(null);
		
		fValid = true;
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		ConfigModeMap.INSTANCE.clear();
		ConfigModeMap.INSTANCE.save(configuration);
		
		validateTab();
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		ConfigModeMap.INSTANCE.load(configuration);

		fContentProvider = CompositeLaunchModesContentProvider.INSTANCE;		
		fLabelProvider = new CompositeLaunchModesLabelProvider();		
		fViewer.setLabelProvider(fLabelProvider);
		fViewer.setContentProvider(fContentProvider);
		fViewer.setInput(ConfigModeMap.INSTANCE);

		validateTab();
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		CompositeLaunchModesContentProvider.INSTANCE.szModes(ConfigModeMap.INSTANCE);
		ConfigModeMap.INSTANCE.save(configuration);
		
		validateTab();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	@Override
	public String getName() {
		return "Launch modes";
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 */
	@Override
	public String getId() {
		return "ro.aquacola.compositelaunch.CompositeLaunchModesTab"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse
	 * .debug.core.ILaunchConfiguration)
	 */
	@Override
	public boolean isValid(ILaunchConfiguration config) {
		validateTab();
		return fValid;
	}
}
