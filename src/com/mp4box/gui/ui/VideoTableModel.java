package com.mp4box.gui.ui;

import javax.swing.table.AbstractTableModel;

/**
 * Source: http://docs.oracle.com/javase/tutorial/uiswing/components/table.html
 */
public class VideoTableModel extends AbstractTableModel {
    
	private static final long serialVersionUID = -848761246074864146L;
	
	private String[] columnNames = {"File", "Chapter", "Chapter Name"};
    private Object[][] data = {};

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Set EEEVERYTHING Editable!
     * Ref: http://gifrific.com/wp-content/uploads/2012/08/Gary-Oldman-Yelling-Everyone-Leon-The-Professional.gif
     */
    public boolean isCellEditable(int row, int col) {
    	return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public void addRow(String file, Boolean chapter, String chapterName){
		Object[][] oldData = data;

		Object[][] newData = new Object[oldData.length+1][3];
		for(int row=0;row<(oldData.length);row++){
			newData[row][0] = oldData[row][0];
			newData[row][1] = oldData[row][1];
			newData[row][2] = oldData[row][2];
		}
		
		//Adding new row
		newData[oldData.length][0] = file;
		newData[oldData.length][1] = chapter;
		newData[oldData.length][2] = chapterName;
		
		data = newData;
		fireTableDataChanged();
    }
    
    public void removeAllRows(){
    	data = new Object[0][0];
    	fireTableDataChanged();
    }
    
    /**
     * Column 0 = File
	 * Column 1 = Chapter
	 * Column 2 = Chapter Name
     * @return
     */
    public Object[][] getData(){
    	return data;
    }
}