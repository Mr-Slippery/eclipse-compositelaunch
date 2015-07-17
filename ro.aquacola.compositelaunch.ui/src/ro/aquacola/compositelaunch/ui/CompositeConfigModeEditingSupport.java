package ro.aquacola.compositelaunch.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ro.aquacola.compositelaunch.ICompositeConfigMode;

public final class CompositeConfigModeEditingSupport extends EditingSupport {
    
    private ComboBoxViewerCellEditor cellEditor = null;
    private Runnable fListener = null;
    
    public CompositeConfigModeEditingSupport(ColumnViewer viewer, Runnable listener) {
        super(viewer);
        cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
        cellEditor.setLabelProvider(new LabelProvider());
        cellEditor.setContentProvider(new ArrayContentProvider());
        fListener = listener;
    }
    
    @Override
    protected CellEditor getCellEditor(Object element) {
        ICompositeConfigMode cm = (ICompositeConfigMode) element;
        cellEditor.setInput(cm.getChoices());
        return cellEditor;
    }
    
    @Override
    protected boolean canEdit(Object element) {
        return true;
    }
    
    @Override
    protected Object getValue(Object element) {
        if (element instanceof String) {
            String data = (String)element;
            return data;
        }
        return null;
    }
    
    @Override
    protected void setValue(Object element, Object value) {
    	if (!(value instanceof String)) { return; }
    	if (((String) value).isEmpty()) { return; }
    	ICompositeConfigMode cm = (ICompositeConfigMode) element;
        /* only set new value if it differs from old one */
        if (cm != null && cm.getMode() != null && !cm.getMode().equals(value)) {
            cm.setMode(value);
            this.getViewer().update(element, null);
            fListener.run();
        }
    }
    
}