package br.jabarasca.postgrefrontend;

import br.jabarasca.postgrefrontend.gui.MainJFrame;
import br.jabarasca.postgrefrontend.gui.SelectPanel;
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
		mainFrame.changeScreenPane(new SelectPanel(mainFrame));
		DataBase db = new DataBase(connAddress, connPort, connUserName, connPwdUser);
		db.closeConnection();
	}
}
