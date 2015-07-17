package ro.aquacola.compositelaunch.ui;

import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchViewerComparator;

import ro.aquacola.compositelaunch.CompositeLaunchActivator;
import ro.aquacola.compositelaunch.CompositeLaunchConfigContentProvider;
import ro.aquacola.compositelaunch.ConfigModeMap;
import ro.aquacola.compositelaunch.internal.ui.CompositeLaunchCategoryFilter;
import ro.aquacola.compositelaunch.internal.ui.CompositeLaunchIdFilter;
import ro.aquacola.compositelaunch.internal.ui.CompositeLaunchUIMessages;
import ro.aquacola.compositelaunch.internal.ui.IInternalDebugUIConstants;
import ro.aquacola.compositelaunch.internal.ui.SWTFactory;

/**
 * Composite launch configuration selection tab.
 * 
 * Initially based on the "Export (Launch Configurations)" dialog of Eclipse Luna.
 *  
 * @author Cornel Izbasa <cizbasa@info.uvt.ro>
 */
public class CompositeLaunchConfigurationTab extends
		AbstractLaunchConfigurationTab {
	
	/**
	 * The check tree viewer for configuration selection - the main control for this tab.
	 */
	private CheckboxTreeViewer fViewer = null;
	
	/**
	 * Content provider for {@link #fViewer}.
	 */
	private CompositeLaunchConfigContentProvider fContentProvider = null;
	
	/**
	 * Validity status for this tab.
	 */
	private boolean fValid = false;
	
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
	 * Set all tree elements to the given state.
	 * 
	 * @param state the given state
	 */
	private void setAllChecked(boolean state) {
		Object[] items = fContentProvider.getElements(fViewer
				.getInput());
		for (int i = 0; i < items.length; i++) {
			fViewer.setSubtreeChecked(items[i], state);
		}
	}

	/**
	 * Creates the check tree viewer portion of the control
	 * 
	 * @param parent the parent to add the check tree viewer to
	 */
	protected void createViewer(Composite parent) {
		SWTFactory.createWrapLabel(parent,
				CompositeLaunchUIMessages.CompositeLaunchUI_LaunchConfigurations, 2);
		Tree tree = new Tree(parent, SWT.BORDER | SWT.SINGLE | SWT.CHECK);
		GridData gd = new GridData(SWT.BEGINNING, SWT.FILL, true, true);
		gd.horizontalSpan = 1;
		tree.setLayoutData(gd);
		fViewer = new CheckboxTreeViewer(tree);
		fViewer.setLabelProvider(DebugUITools.newDebugModelPresentation());
		fViewer.setComparator(new WorkbenchViewerComparator());
		fContentProvider = CompositeLaunchConfigContentProvider.INSTANCE;
		fViewer.setContentProvider(fContentProvider);
		fViewer.setInput(DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationTypes());
		
		// we don't want to see builders....
		fViewer.addFilter(new CompositeLaunchCategoryFilter(
				IInternalDebugUIConstants.ID_EXTERNAL_TOOL_BUILDER_LAUNCH_CATEGORY));
		
		// we don't want to see Composite launch configs -
		// could be added together with recursion checks later.
		String compositeLaunchId = Platform.getResourceString(CompositeLaunchActivator.getDefault().getBundle(), "%ro.aquacola.compositelaunch.id");
		fViewer.addFilter(new CompositeLaunchIdFilter(compositeLaunchId));

		// need to force load the children so that select all works initially
		fViewer.expandAll();
		fViewer.collapseAll();

		fViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateCheckedState(event.getElement());
				updateLaunchConfigurationDialog();
			}
		});
		Composite selectDeselectAll = SWTFactory.createComposite(parent,
				parent.getFont(), 1, 1, GridData.BEGINNING, 0, 0);
		Button selectAll = SWTFactory.createPushButton(selectDeselectAll,
				CompositeLaunchUIMessages.CompositeLaunchUI_SelectAll, null);
		selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAllChecked(true);
				updateLaunchConfigurationDialog();
			}
		});
		Button deselectAll = SWTFactory.createPushButton(selectDeselectAll,
				CompositeLaunchUIMessages.CompositeLaunchUI_DeselectAll, null);
		deselectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setAllChecked(false);
				updateLaunchConfigurationDialog();
			}
		});
	}

	/**
	 * Updates the checked state of child launch configurations if the parent
	 * type is checked
	 * 
	 * @param item
	 */
	protected void updateCheckedState(Object element) {
		boolean state = fViewer.getChecked(element);
		if (element instanceof ILaunchConfigurationType) {
			Object[] items = fContentProvider.getChildren(element);
			for (int i = 0; i < items.length; i++) {
				fViewer.setChecked(items[i], state);
			}
			fViewer.setGrayed(element, false);
		} else if (element instanceof ILaunchConfiguration) {
			updateParentChecked(element);
		}
	}

	/** 
	 * Validation check for this tab.
	 * 
	 * Currently translates to having at least a launch configuration selected in the check tree.  
	 */
	private void validateTab() {
		setErrorMessage(null);
		setMessage(null);

		isComplete();		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// Start with an empty map of launch configs and modes.
		ConfigModeMap.INSTANCE.clear();
		ConfigModeMap.INSTANCE.save(configuration);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		ConfigModeMap.INSTANCE.load(configuration);
		
		Object[] items = fContentProvider.getElements(null);
		for (Object item : items) {
			initializeCheckedState(item, ConfigModeMap.INSTANCE.configs());
			if (fViewer.getChecked(item)) {
				fViewer.expandToLevel(item, CheckboxTreeViewer.ALL_LEVELS);
			}
		}

		validateTab();
	}

	/**
	 * Update the parent's checked state based on changes to the child element.
	 * 
	 * @param element the child element
	 */
	private void updateParentChecked(Object element) {
		Object parent = fContentProvider.getParent(element);
		Object[] items = fContentProvider.getChildren(parent);
		boolean checked = true;
		boolean onechecked = false;
		for (int i = 0; i < items.length; i++) {
			boolean state = fViewer.getChecked(items[i]);
			checked &= state;
			if (state) {
				onechecked = true;
			}
		}
		fViewer.setGrayed(parent, onechecked & !checked);
		fViewer.setChecked(parent, checked | onechecked);
	}

	/**
	 * Initialize the checked state of the subtree elements based on the set of launch configurations.
	 * 
	 * @param element the root of the subtree
	 * @param checked the launch configurations set
	 */
	protected void initializeCheckedState(Object element, Set<ILaunchConfiguration> checked) {
		if (element instanceof ILaunchConfigurationType) {
			Object[] items = fContentProvider.getChildren(element);
			for (int i = 0; i < items.length; i++) {
				initializeCheckedState(items[i], checked);
			}
		} else if (element instanceof ILaunchConfiguration) {
			fViewer.setChecked(element, checked.contains(element));
			updateParentChecked(element);
		}
	}

	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		Object[] checked = fViewer.getCheckedElements();		
		ConfigModeMap.INSTANCE.save(configuration, checked);	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	@Override
	public String getName() {
		return "Composite configuration";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 */
	@Override
	public String getId() {
		return "ro.aquacola.compositelaunch.CompositeLaunchConfigurationTab"; //$NON-NLS-1$
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

	/**
	 * Returns if the tree selection is complete - at least one configuration selected.
	 * 
	 * @return true if at least one configuration is selected.
	 */
	protected void isComplete() {
		Object[] elements = fViewer.getCheckedElements();
		boolean oneconfig = false;
		fValid = false;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof ILaunchConfiguration) {
				oneconfig = true;
				break;
			}
		}
		if (elements.length < 1 || !oneconfig) {
			setErrorMessage(CompositeLaunchUIMessages.CompositeLaunchUI_SelectOne);
			return;
		}

		fValid = true;
	}
}
