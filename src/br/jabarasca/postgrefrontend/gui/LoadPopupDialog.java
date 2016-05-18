package br.jabarasca.postgrefrontend.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.SwingConstants;

public class LoadPopupDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final int DESIRED_SCREEN_WIDTH = 350;
	private final int DESIRED_SCREEN_HEIGHT = 100;
	
	public LoadPopupDialog(int nativeWidth, int nativeHeight) {
		final int X = 0;
		final int Y = 1;
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		int[] screenProps = setScreenStartProperties(nativeWidth, nativeHeight);
		setBounds(screenProps[X], screenProps[Y], DESIRED_SCREEN_WIDTH, DESIRED_SCREEN_HEIGHT);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		
		JLabel lblWait = new JLabel(GuiStrings.LOAD_DIALOG_MESSAGE);
		lblWait.setHorizontalAlignment(SwingConstants.CENTER);
		lblWait.setFont(new Font(GuiStrings.APP_FONT_STYLE, Font.ITALIC, 26));
		lblWait.setBounds(25, DESIRED_SCREEN_HEIGHT/5, 300, 40);
		contentPanel.add(lblWait);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
	}
	
	private int[] setScreenStartProperties(int nativeWidth, int nativeHeight) {
		return new int[]{(nativeWidth/2) - (DESIRED_SCREEN_WIDTH/2), 
				(nativeHeight/2) - (DESIRED_SCREEN_HEIGHT/2)};
	}
}