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
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;

public class AppPanel extends JPanel {

	private MainJFrame mainFrame;
	private final int INITIAL_Y_START = 20;
	private int lastYPosition = 0;
	private int lastXPosition;
	private JTextField portNumberTextField;
	private JTextField addressTextField;
	/**
	 * Create the panel.
	 */
	public AppPanel(MainJFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		JLabel labelTitle = new JLabel(GuiStrings.APPLICATION_TITLE);
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 24));
		lastYPosition += INITIAL_Y_START;
		labelTitle.setBounds(getCenterXStart(300), lastYPosition, 300, 30);
		add(labelTitle);
		
		JLabel labelConnDetails = new JLabel(GuiStrings.CONN_DETAILS_LABEL);
		labelConnDetails.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 18));
		lastYPosition += 60;
		labelConnDetails.setBounds(getCenterXStart(150), lastYPosition, 150, 20);
		add(labelConnDetails);
		
		JRadioButton defaultConnRadio = new JRadioButton(GuiStrings.LOCAL_CONN_LABEL);
		defaultConnRadio.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.PLAIN, 16));
		lastYPosition += 60;
		defaultConnRadio.setBounds(getCenterXStart(240)/4, lastYPosition, 240, 23);
		add(defaultConnRadio);
		
		JRadioButton newConnRadio = new JRadioButton(GuiStrings.NEW_CONN_LABEL);
		newConnRadio.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.PLAIN, 16));
		newConnRadio.setBounds((getCenterXStart(240)/4)*7, lastYPosition, 240, 23);
		add(newConnRadio);
		
		lastYPosition += 60;
		lastXPosition = 80;
		JLabel connAddressLabel = new JLabel(GuiStrings.ADDRESS_LABEL);
		connAddressLabel.setBounds(lastXPosition, lastYPosition, 70, 15);
		connAddressLabel.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 14));
		add(connAddressLabel);
		
		lastXPosition += 80;
		addressTextField = new JTextField();
		addressTextField.setBounds(lastXPosition, lastYPosition, 200, 25);
		add(addressTextField);
		addressTextField.setColumns(10);
		
		lastXPosition += 240;
		JLabel portNumberLabel = new JLabel(GuiStrings.PORT_NUMBER_LABEL);
		portNumberLabel.setBounds(lastXPosition, lastYPosition, 70, 15);
		portNumberLabel.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 14));
		add(portNumberLabel);
		
		lastXPosition += 50;
		portNumberTextField = new JTextField();
		portNumberTextField.setBounds(lastXPosition, lastYPosition, 60, 25);
		add(portNumberTextField);
		portNumberTextField.setColumns(10);
		
		lastYPosition += 60; 
		JButton connectButton = new JButton(GuiStrings.CONNECT_LABEL);
		connectButton.setBounds(getCenterXStart(117), lastYPosition, 117, 25);
		add(connectButton);

	}
	
	private int getCenterXStart(int componentWidth) {
		return mainFrame.X_CENTER - (componentWidth/2);
	}
}
