package diploma;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class TableModelForManager extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] columnNames = 
		{"Round", "Your proposal","Response from Manager" ,"Your profit"};
	@SuppressWarnings("rawtypes")
	private static final Class[] columnClasses = 
		{Integer.class, Double.class, Double.class, Double.class};
	private ArrayList<ManageInfo> _managerInfo;
	
	
	 public TableModelForManager(ArrayList<ManageInfo> _managerInfo) {
	
        this._managerInfo = _managerInfo;

    }
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return _managerInfo.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	@Override
	public String getColumnName(int col){
		return columnNames[col];
	}
	
	
	public Class<?>  getColumnClass(int col){
		return columnClasses[col];
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		ManageInfo _manager= _managerInfo.get(rowIndex);
		switch(columnIndex){
		case 0:
			return _manager.get_round()-1;
		case 1: 	
			return _manager.get_proposal(); 
		case 2:
			return _manager.get_response();
		case 3:
			return _manager.get_profit();
		
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int row, int count){
		return false;
		}
	

	
	
	

}
