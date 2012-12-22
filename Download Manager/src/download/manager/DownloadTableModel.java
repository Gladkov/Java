package download.manager;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

public class DownloadTableModel extends AbstractTableModel implements Observer{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] columnNames = 
		{"URL", "Size", "Progress", "Status"};
	@SuppressWarnings("rawtypes")
	private static final Class[] columnClasses = 
		{String.class,String.class, JProgressBar.class, String.class};
	private ArrayList<Download> downloadList = new ArrayList<Download>();
	
	public void addDownload(Download download)
	{
		download.addObserver(this);
		downloadList.add(download);
		
		fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
		
		
	}
	
	public Download getDownload(int row)
	{
		return downloadList.get(row);
		
		
	}
	
	public void clearDownload(int row)
	{
		downloadList.remove(row);
		fireTableRowsDeleted(row, row);
		
	}
	@Override
	public String getColumnName(int col){
		return columnNames[col];
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Class getColumnClass(int col){
		return columnClasses[col];
	}

	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {

		return downloadList.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Download download = downloadList.get(row);
		switch(col){
		case 0:
			return download.getURL();
		case 1: 
			int size = download.getSize(); 
			return (size == -1) ? "" : Integer.toString(size); 
		case 2: return new Float(download.getProgress());
		case 3: return Download.Status[download.getStatus()];
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		int index = downloadList.indexOf(o);
		
		fireTableRowsUpdated(index, index);
	}
	
	

}
