package com.catlinman.rotcrypt;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.io.IOException;
import java.net.URI;
import java.text.NumberFormat;

public class Program implements ActionListener, FocusListener, WindowListener, MouseListener{
	private JFrame mainFrame;
	private JFrame aboutFrame;
	private JTextArea inputArea;
	private JTextArea outputArea;
    private JLabel rotationLabel;
    private JLabel rotationArrayLabel;
    private JLabel urlLabel;
	private JFormattedTextField rotField;
	private JFormattedTextField rotArrayField;
	private Rotator rotator;
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
            public void run(){
        		Program program = new Program();
        		program.init();
            }
        });
	}
	
	public void init(){
		rotator = new Rotator();
		initializeMainFrame();
		initializeAboutFrame();
	}
	
	public void initializeMainFrame(){
		mainFrame = new JFrame("Rotcrypt");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(640, 520));
        mainFrame.setPreferredSize(new Dimension(640, 520));
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        
        JLabel inputLabel = new JLabel("Normal text");
        JLabel outputLabel = new JLabel("Rotated text");
        
        rotationLabel = new JLabel("Rotations: ");
        rotationArrayLabel = new JLabel("Rotation array (Seperate rotations with a comma)");
        rotationArrayLabel.setEnabled(false);
        
        inputArea = new JTextArea("Rotate me!",10, 52);
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);
        inputArea.setCaretPosition(inputArea.getText().length());
        
        outputArea = new JTextArea("",10, 52);
        outputArea.setWrapStyleWord(true);
        outputArea.setLineWrap(true);
        
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        
		NumberFormat digitformat = NumberFormat.getInstance();
		digitformat.setMinimumIntegerDigits(1);
		digitformat.setMaximumIntegerDigits(2);
        
        rotField = new JFormattedTextField(digitformat);
        rotField.setValue(new Long(rotator.getRot()));
        rotField.setColumns(2);
        
        rotArrayField = new JFormattedTextField();
        rotArrayField.setText(rotator.rotArray);
        rotArrayField.setColumns(52);
        rotArrayField.setEnabled(false);
        rotArrayField.addFocusListener(this);
        
        JCheckBox useArrayCheckbox = new JCheckBox("Use rotation array");
        useArrayCheckbox.setMnemonic(KeyEvent.VK_C); 
        useArrayCheckbox.setSelected(false);
        useArrayCheckbox.addActionListener(this);
        useArrayCheckbox.setActionCommand("checkbox");
        
        JButton rotateButton = new JButton("Normal to rotated");
        rotateButton.setMnemonic(KeyEvent.VK_DOWN); 
        rotateButton.addActionListener(this);
        rotateButton.setActionCommand("encrypt");
        
        JButton unrotateButton = new JButton("Rotated to normal");
        unrotateButton.setMnemonic(KeyEvent.VK_UP); 
        unrotateButton.addActionListener(this);
        unrotateButton.setActionCommand("decrypt");
        
        JButton helpButton = new JButton("About");
        helpButton.addActionListener(this);
        helpButton.setActionCommand("about");
        
        panel.add(inputLabel);
        panel.add(inputScrollPane);
        panel.add(outputLabel);
        panel.add(outputScrollPane);
        panel.add(Box.createRigidArea(new Dimension(640,5)));
        panel.add(helpButton);
        panel.add(rotateButton);
        panel.add(unrotateButton);
        panel.add(Box.createRigidArea(new Dimension(10,0)));
        panel.add(rotationLabel);
        panel.add(rotField);
        panel.add(Box.createRigidArea(new Dimension(10,0)));
        panel.add(useArrayCheckbox);
        panel.add(Box.createRigidArea(new Dimension(640,5)));
        panel.add(rotationArrayLabel);
        panel.add(rotArrayField);
        
        mainFrame.add(panel);
        mainFrame.pack();
        mainFrame.setVisible(true);
	}
	
	public void initializeAboutFrame(){
		aboutFrame = new JFrame("Rotcrypt - About");
		aboutFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		aboutFrame.setSize(new Dimension(480, 440));
		aboutFrame.setPreferredSize(new Dimension(480, 440));
		aboutFrame.setResizable(false);
		aboutFrame.setLocationRelativeTo(null);
		aboutFrame.setVisible(false);
        aboutFrame.addWindowListener(this);
        
        JPanel panel = new JPanel();
        
        String infoString = ("-LEGAL-\n"
        		+ "Rotcrypt is provided free of charge and can be freely redistributed as long as it is not altered in any way."
        		+ " You are not allowed to redistribute Rotcrypt claiming that it was created by someone other than its creator."
        		+ " You are also not allowed to sell this product without the creators permission. If you do however plan to use this program in"
        		+ " some sort of commercial context, please ask me for permission first.\n\n"
        		+ "-HOW TO USE-\n"
        		+ "Using Rotcrypt is quite simple. Just enter something in the 'normal text' field and press the 'normal to rotated' button."
        		+ " Depending on your designated amount of rotations in the rotations box, each letters alphabetical index will be moved by that amount causing the letter to change.\n\n"
        		+ "As an example: Entering 'abcd' in the 'normal text' field and setting the 'rotations' to one will output 'bcde'."
        		+ " The reason for this is that every letters index is moved by one space:\n\n"
        		+ "INDEX:     1234   ---> 2345\n"
        		+ "LETTER:  ABCD ---> BCDE\n\n"
        		+ " You can now reverse the process if you press the 'rotated to normal' button. To do this however you"
        		+ " need to have the original number of rotations that were specified when the text was originaly rotated.\n\n"
        		+ "The checkbox 'use rotation array' can be used to specify a sequence of rotations that make it even harder to"
        		+ " revert the text back to its original when the sequence is unknown. This can give your text another layer of"
        		+ " protection due to the fact that more information is needed to deobfuscate the text.\n");
        		
        		
        
        JTextArea infoText = new JTextArea(infoString, 19, 36);
        infoText.setWrapStyleWord(true);
        infoText.setLineWrap(true);
        infoText.setEditable(false);
        
        JScrollPane infoTextScrollPane = new JScrollPane(infoText);
        
        JLabel creditLabel = new JLabel("Rotcrypt was created by");
        creditLabel.setFont(new Font("Arial Bold", Font.PLAIN, 16));
        
        urlLabel = new JLabel("@Catlinman_");
        urlLabel.setForeground(new Color(75, 75, 255));
        urlLabel.addMouseListener(this);
        urlLabel.setFont(new Font("Arial Bold", Font.PLAIN, 16));
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        closeButton.setActionCommand("closeAbout");
        
        panel.add(Box.createRigidArea(new Dimension(640,10)));
        panel.add(infoTextScrollPane);
        panel.add(Box.createRigidArea(new Dimension(640,1)));
        panel.add(creditLabel);
        panel.add(urlLabel);
        panel.add(Box.createRigidArea(new Dimension(640,1)));
        panel.add(closeButton);
        
        aboutFrame.add(panel);
        aboutFrame.pack();
	}
	
	public String numberfilter(String s){
		return(s.replaceAll("[^0123456789,]", ""));
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand() == "encrypt"){
			if(inputArea.getText().length() > 0){
				if(!rotator.useArray){
					rotField.setValue((long) rotField.getValue() % 26);
					rotator.setRot((long) rotField.getValue());
					outputArea.replaceRange(rotator.rotate(inputArea.getText(), false), 0, outputArea.getText().length());
				}
				else{
					rotator.rotArray = rotArrayField.getText();
					outputArea.replaceRange(rotator.rotate(inputArea.getText(), false), 0, outputArea.getText().length());
				}
			}
		}
		
		else if(e.getActionCommand() == "decrypt"){
			if(outputArea.getText().length() > 0){
				if(!rotator.useArray){
					rotField.setValue((long) rotField.getValue() % 26);
					rotator.setRot((long) rotField.getValue());
					inputArea.replaceRange(rotator.rotate(outputArea.getText(), true), 0, inputArea.getText().length());
				}
				else{
					rotator.rotArray = rotArrayField.getText();
					inputArea.replaceRange(rotator.rotate(outputArea.getText(), true), 0, inputArea.getText().length());
				}
			}
		}
		
		else if(e.getActionCommand() == "about"){
			if(!aboutFrame.isVisible()){
				aboutFrame.setLocationRelativeTo(null);
				aboutFrame.setVisible(true);
			}
			else{
				aboutFrame.requestFocus();
			}
		}
		
		else if(e.getActionCommand() == "closeAbout"){
			aboutFrame.dispose();
			mainFrame.requestFocus();
		}
		
		else if(e.getActionCommand() == "checkbox"){
			if(!rotator.useArray){
				rotator.useArray = true;
				rotationLabel.setEnabled(false);
				rotField.setEnabled(false);
				rotArrayField.setEnabled(true);
				rotationArrayLabel.setEnabled(true);
			}
			else{
				rotator.useArray = false;
				rotationLabel.setEnabled(true);
				rotField.setEnabled(true);
				rotationArrayLabel.setEnabled(false);
				rotArrayField.setEnabled(false);
			}
		}
	}

	public void focusGained(FocusEvent e){
		if(e.getSource() == rotArrayField) rotArrayField.setText(numberfilter(rotArrayField.getText()));
	}


	public void focusLost(FocusEvent e){
		if(e.getSource() == rotArrayField) rotArrayField.setText(numberfilter(rotArrayField.getText()));
	}


	public void windowActivated(WindowEvent e) {
		
	}

	public void windowClosed(WindowEvent e) {
		
	}

	public void windowClosing(WindowEvent e) {
		if(e.getSource() == aboutFrame){
			aboutFrame.dispose();
			mainFrame.requestFocus();
		}
	}

	public void windowDeactivated(WindowEvent e) {
		
	}

	public void windowDeiconified(WindowEvent e) {
		
	}

	public void windowIconified(WindowEvent e) {
		
	}

	public void windowOpened(WindowEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == urlLabel){
			String url = "https://twitter.com/catlinman_";
			
	        try {
				Desktop.getDesktop().browse(URI.create(url));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {

	}
}