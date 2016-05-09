package br.jabarasca.postgrefrontend.gui;

import javax.swing.JPanel;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class SelectPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	//private int lastXPosition;
	private int lastYPosition;
	private final int INITIAL_Y_START = 20;
	private MainJFrame mainFrame;
	
	public SelectPanel(MainJFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(null);
		
		lastYPosition = INITIAL_Y_START;
		JLabel titleLabel = new JLabel(GuiStrings.SELECT_TITLE_LABEL);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(this.mainFrame.getCenterXStart(300), lastYPosition, 300, 30);
		titleLabel.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 24));
		add(titleLabel);
		
		lastYPosition += 60;
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(mainFrame.getCenterXStart(400), lastYPosition, 400, 30);
		add(comboBox);
		
		lastYPosition += 60;
		JTextArea textArea = new JTextArea();
		textArea.setBounds(mainFrame.getCenterXStart(600), lastYPosition, 600, 150);
		add(textArea);
		
		lastYPosition += 170;
		JButton btnNewButton = new JButton(GuiStrings.EXPORT_LABEL);
		btnNewButton.setBounds(mainFrame.getCenterXStart(117), lastYPosition, 117, 25);
		add(btnNewButton);

		
	}
}
