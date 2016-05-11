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
	
	public Controller(MainJFrame mainFrame, String address, String port, String userName, String pwdUser) {
		connAddress = address;
		connPort = port;
		connUserName = userName;
		connPwdUser = pwdUser;
		
		this.mainFrame = mainFrame;
	}
	
	public void connect() {
		DataBase db = new DataBase(connAddress, connPort, connUserName, connPwdUser);
		if(db.isDbConnEstablished()) {
			List<String> dbNames = db.getAllDataBases();
			if(dbNames != null) {
				mainFrame.changeScreenPane(new SelectPanel(mainFrame, dbNames));
				db.closeConnection();
			} else {
				mainFrame.setMessageDialog(GuiStrings.GET_DB_NAMES_FAIL);
			}
		} else {
			mainFrame.setMessageDialog(GuiStrings.DB_CONN_FAIL);
		}
	}
}
