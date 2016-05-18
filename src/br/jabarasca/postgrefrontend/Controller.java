package br.jabarasca.postgrefrontend;

import br.jabarasca.postgrefrontend.gui.GuiStrings;
import br.jabarasca.postgrefrontend.gui.MainJFrame;
import br.jabarasca.postgrefrontend.gui.SelectPanel;

import java.util.List;

import javax.swing.SwingUtilities;

import br.jabarasca.postgrefrontend.dao.DataBase;

public class Controller {

	private MainJFrame mainFrame;
	private String connAddress;
	private String connPort;
	private String connUserName;
	private String connPwdUser;
	private String connectedDBName;
	private String outputScript;
	private DataBase db;
	private Runnable generateDDLScriptRun;
	private SelectPanel selectPanel;
	
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
		connectedDBName = dbName;
		if(dbName.length() > 0) {
			setUpGenScriptRunnable();
			if(selectPanel == null) {
				selectPanel = (SelectPanel)mainFrame.getCurrentScreenPanel();
			}
			new Thread(generateDDLScriptRun).start();
			selectPanel.setLoadDialogVisible(true);
		} else {
			mainFrame.setMessageDialog(GuiStrings.SELECT_VALID_DB);
		}
	}
	
	private void setUpGenScriptRunnable() {
		final Runnable ddlExportFailGUIRun = new Runnable() {
			@Override
			public void run() {
				mainFrame.setMessageDialog(GuiStrings.EXPORT_DB_FAIL);
			}
		};
		
		final Runnable ddlExportSuccessGUIRun = new Runnable() {
			@Override
			public void run() {
				selectPanel.setLoadDialogVisible(false);
				selectPanel.setTextAreaValue(outputScript);
			}
		};
		
		if(generateDDLScriptRun == null) {
			generateDDLScriptRun = new Runnable() {
				@Override
				public void run() {
					db.connectToSpecificDB(connectedDBName);
					outputScript = db.getTablesDDLFromConnectedDB();
					if(outputScript == null) {
						SwingUtilities.invokeLater(ddlExportFailGUIRun);
					} else {
						SwingUtilities.invokeLater(ddlExportSuccessGUIRun);
					}
					db.closeConnection();
				}
			};
		}
	}
}
