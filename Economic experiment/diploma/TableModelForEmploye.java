package diploma;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class TableModelForEmploye extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] columnNames = 
		{"Round", "Proposal for you","Your pesponse" ,"Your profit"};
	@SuppressWarnings("rawtypes")
	private static final Class[] columnClasses = 
		{Integer.class, Double.class, Double.class, Double.class};
	private ArrayList<EmployerInfo> _employeInfo;
	
	
	 public TableModelForEmploye(ArrayList<EmployerInfo> _employeInfo) {
	
        this._employeInfo = _employeInfo;

    }
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return _employeInfo.size();
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
		EmployerInfo _employe= _employeInfo.get(rowIndex);
		switch(columnIndex){
		case 0:
			return _employe.get_round();
		case 1: 	
			return _employe.get_proposal(); 
		case 2:
			return _employe.get_response();
		case 3:
			return _employe.get_profit();
		
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int row, int count){
		return false;
		}
	

	
	
	

}
