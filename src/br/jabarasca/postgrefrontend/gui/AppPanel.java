package br.jabarasca.postgrefrontend.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

public class AppPanel extends JPanel {

	private MainJFrame mainFrame;
	private final int INITIAL_Y_START = 20;
	/**
	 * Create the panel.
	 */
	public AppPanel(MainJFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		JLabel labelTitle = new JLabel(GuiStrings.APPLICATION_TITLE);
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setFont(new Font("Droid Sans", Font.BOLD, 24));
		labelTitle.setBounds(getCenterXStart(300), INITIAL_Y_START, 300, 30);
		add(labelTitle);

	}
	
	private int getCenterXStart(int componentWidth) {
		return mainFrame.X_CENTER - (componentWidth/2);
	}
}
