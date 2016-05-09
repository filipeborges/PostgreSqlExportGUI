package br.jabarasca.postgrefrontend;

import br.jabarasca.postgrefrontend.gui.MainJFrame;
import br.jabarasca.postgrefrontend.gui.SelectPanel;

public class Controller {

	private MainJFrame mainFrame;
	
	public Controller(MainJFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public void connect() {
		mainFrame.changeScreenPane(new SelectPanel(mainFrame));
	}
}
