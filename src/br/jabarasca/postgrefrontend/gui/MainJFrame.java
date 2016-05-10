package br.jabarasca.postgrefrontend.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainJFrame extends JFrame {

	private JPanel contentPane;
	public final int X_RESOLUTION = 640;
	public final int Y_RESOLUTION = 360;
	public final int X_CENTER = X_RESOLUTION / 2;
	
	private int screenStartX;
	private int screenStartY;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJFrame frame = new MainJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	private void setScreenStartProperties() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		screenStartX = (dimension.width/2) - (X_RESOLUTION/2);
		screenStartY = (dimension.height/2) - (Y_RESOLUTION/2);
	}
	
	/**
	 * Create the frame.
	 */
	public MainJFrame() {
		setScreenStartProperties();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(screenStartX, screenStartY, X_RESOLUTION, Y_RESOLUTION);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(new ConnectPanel(this));
		
		setContentPane(contentPane);
		
	}
	
	public void changeScreenPane(JPanel jpanel) {
		contentPane.removeAll();
		contentPane.add(jpanel);
		contentPane.revalidate(); 
		contentPane.repaint();
	}

	public int getCenterXStart(int componentWidth) {
		return X_CENTER - (componentWidth/2);
	}
}
