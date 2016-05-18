package br.jabarasca.postgrefrontend.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import br.jabarasca.postgrefrontend.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class ConnectPanel extends JPanel {

	private MainJFrame mainFrame;
	private final int INITIAL_Y_START = 20;
	private int lastYPosition = 0;
	private int lastXPosition;
	private JTextField portNumberTextField;
	private JTextField addressTextField;
	private JButton connectButton;
	private JTextField userNameTextField;
	private JPasswordField passwordField;
	/**
	 * Create the panel.
	 */
	public ConnectPanel(MainJFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		JLabel labelTitle = new JLabel(GuiStrings.APPLICATION_TITLE);
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 24));
		lastYPosition += INITIAL_Y_START;
		labelTitle.setBounds(mainFrame.getCenterXStart(300), lastYPosition, 300, 30);
		add(labelTitle);
		
		lastYPosition += 80;
		lastXPosition = 100;
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
		
		lastXPosition = 110;
		lastYPosition += 80;
		JLabel userNameLabel = new JLabel(GuiStrings.USER_NAME_LABEL);
		userNameLabel.setBounds(lastXPosition, lastYPosition, 170, 15);
		userNameLabel.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 14));
		add(userNameLabel);
		
		lastXPosition += 170;
		userNameTextField = new JTextField();
		userNameTextField.setBounds(lastXPosition, lastYPosition, 200, 25);
		add(userNameTextField);
		userNameTextField.setColumns(10);
		
		lastXPosition = 182;
		lastYPosition += 50;
		JLabel userPwdLabel = new JLabel(GuiStrings.USER_PWD_LABEL);
		userPwdLabel.setBounds(lastXPosition, lastYPosition, 90, 15);
		userPwdLabel.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 14));
		add(userPwdLabel);
		
		lastXPosition += 98;
		passwordField = new JPasswordField();
		passwordField.setBounds(lastXPosition, lastYPosition, 200, 25);
		add(passwordField);
		
		lastYPosition += 70; 
		connectButton = new JButton(GuiStrings.CONNECT_LABEL);
		connectButton.setBounds(mainFrame.getCenterXStart(117), lastYPosition, 117, 25);
		add(connectButton);

		setConnectButtonListener();
	}
	
	private void setConnectButtonListener() {
		ActionListener actionConnect = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller controller = new Controller(mainFrame, addressTextField.getText(),
						portNumberTextField.getText(), userNameTextField.getText(), 
						passwordField.getText());
				controller.connect();
			}
		};
		connectButton.addActionListener(actionConnect);
	}
}
