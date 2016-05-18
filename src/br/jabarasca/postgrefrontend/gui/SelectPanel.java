package br.jabarasca.postgrefrontend.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import br.jabarasca.postgrefrontend.Controller;

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
	private JComboBox<String> comboBox;
	private JButton exportButton;
	private JTextArea scriptTextArea;
	private String dbNameSelected = "";
	private Controller controller;
	private LoadPopupDialog popDialog;
	
	public SelectPanel(MainJFrame mainFrame, List<String> dbNames, Controller controller) {
		this.mainFrame = mainFrame;
		this.controller = controller;
		setLayout(null);
		
		popDialog = new LoadPopupDialog(mainFrame.getNativeScreenWidth(),
					mainFrame.getNativeScreenHeight());
		
		lastYPosition = INITIAL_Y_START;
		JLabel titleLabel = new JLabel(GuiStrings.SELECT_TITLE_LABEL);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(this.mainFrame.getCenterXStart(300), lastYPosition, 300, 30);
		titleLabel.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.BOLD, 24));
		add(titleLabel);
		
		lastYPosition += 60;
		comboBox = new JComboBox<String>();
		comboBox.setBounds(mainFrame.getCenterXStart(400), lastYPosition, 400, 30);
		add(comboBox);
		
		lastYPosition += 60;
		scriptTextArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(scriptTextArea);
		scroll.setBounds(mainFrame.getCenterXStart(600), lastYPosition, 600, 150);
		add(scroll);
		
		lastYPosition += 170;
		exportButton = new JButton(GuiStrings.EXPORT_LABEL);
		exportButton.setBounds(mainFrame.getCenterXStart(117), lastYPosition, 117, 25);
		add(exportButton);

		setDbNames(dbNames);
		setComboBoxListener();
		setExportBtnListener();
	}
	
	public void setLoadDialogVisible(boolean isVisible) {
		popDialog.setVisible(isVisible);
	}
	
	public void setTextAreaValue(String text) {
		scriptTextArea.setText(text);
	}
	
	private void setExportBtnListener() {
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.export(dbNameSelected);
			}
		};
		exportButton.addActionListener(action);
	}
	
	private void setComboBoxListener() {
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actEv) {
				JComboBox cb = (JComboBox)actEv.getSource();
				String dbName = (String)cb.getSelectedItem();
				if(dbName != GuiStrings.DEFAULT_COMBO_VALUE) {
					dbNameSelected = dbName;
				} else {
					dbNameSelected = "";
					mainFrame.setMessageDialog(GuiStrings.SELECT_VALID_DB);
				}
			}
		};
		comboBox.addActionListener(action);
	}
	
	private void setDbNames(List<String> dbNames) {
		String dbName;
		comboBox.addItem(GuiStrings.DEFAULT_COMBO_VALUE);
		for(int i = 0; i < dbNames.size(); i++) {
			dbName = dbNames.get(i);
			if(dbName != null) {
				comboBox.addItem(dbName);
			}
		}
	}
}
