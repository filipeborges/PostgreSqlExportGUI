package br.jabarasca.postgrefrontend;

import br.jabarasca.postgrefrontend.gui.GuiStrings;
import br.jabarasca.postgrefrontend.gui.MainJFrame;
import br.jabarasca.postgrefrontend.gui.SelectPanel;

import java.util.List;

import br.jabarasca.postgrefrontend.dao.DataBase;

public class Controller {

	private MainJFrame mainFrame;
	private String connAddress;
	private String connPort;
	private String connUserName;
	private String connPwdUser;
	private DataBase db;
	
	public Controller(MainJFrame mainFrame, String address, String port, String userName, String pwdUser) {
		connAddress = address;
		connPort = port;
		connUserName = userName;
		connPwdUser = pwdUser;
		
		this.mainFrame = mainFrame;
	}
	
	public void connect() {
		db = new DataBase(connAddress, connPort, connUserName, connPwdUser);
		if(db.isDbConnEstablished()) {
			List<String> dbNames = db.getAllDataBases();
			if(dbNames != null) {
				mainFrame.changeScreenPane(new SelectPanel(mainFrame, dbNames, this));
				db.closeConnection();
			} else {
				mainFrame.setMessageDialog(GuiStrings.GET_DB_NAMES_FAIL);
			}
		} else {
			mainFrame.setMessageDialog(GuiStrings.DB_CONN_FAIL);
		}
	}
	
	public void export(String dbName) {
		if(dbName.length() > 0) { 
			db.connectToSpecificDB(dbName);
			//List<String> dbTables = db.getAllTablesFromConnectedDB();
			String outputScript = db.getTablesDDLFromConnectedDB();
			SelectPanel selectPanel = (SelectPanel)mainFrame.getCurrentScreenPanel();
			selectPanel.setTextAreaValue(outputScript);
			db.closeConnection();
		} else {
			mainFrame.setMessageDialog(GuiStrings.SELECT_VALID_DB);
		}
	}
	
	private String getFormattedScript(List<String> scriptData) {
		String formattedScript = "";
		
		for(int i = 0; i < scriptData.size(); i++) {
			formattedScript += String.format("%s\n", scriptData.get(i));
		}
		
		return formattedScript;
	}
}
