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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URI;
import java.text.NumberFormat;

public class Program implements ActionListener, FocusListener, WindowListener, MouseListener {
	private JFrame mainFrame;
	private JFrame aboutFrame;
	private JTextArea inputArea;
	private JTextArea outputArea;
	private JLabel rotationLabel;
	private JLabel rotationArrayLabel;
	private JLabel urlLabel;
	private JFormattedTextField rotField;
	private JFormattedTextField rotArrayField;
	
	private Rotator rotator; // Variable storing the rotator used within the program.
	private boolean arrayEnabled = false; // Stores the GUI state of the array check-box.
	
	public static void main(String[] args){
		// Command line section. This part is run when at least one argument is passed.
		if(args.length > 0){
			File inputFile = new File(args[0]); // Path to the file to be rotated.
			String inputRotation = "13"; // Stores the rotation string which is later on supplied to the rotator.
			
			// Exception handle to check if the given filepath is actually valid.
			try{
				if(inputFile.exists()){
					// Get the rotation or rotation array from the second argument. If none is present, default to 13.
					if(args.length > 1){
						inputRotation = args[1].replaceAll("[^0123456789,-]", "");
					} else{
						System.out.println("Rotations not specified - using 13 as the default");
					}
					
					File outputFile = inputFile; // File storing the rotated contents of the input file.
					String[] tokens = args[0].split("\\.(?=[^\\.]+$)"); // Regex to split the path into a root path and it's extension.
					
					try{
						// Make the path of the output file and create the file. Catch any exceptions at the same time.
						outputFile = new File(tokens[0] + "_r." + tokens[1]);
						outputFile.createNewFile();
					} catch(IOException e){
						e.printStackTrace();
					}
					
					// Prepare the variables needed for traversing and rotating the input file. Also initialize the rotator.
					String readString, writeString, nextString;
					BufferedReader br = null;
					BufferedWriter bw = null;
					Rotator rotator = new Rotator();
					
					rotator.setRotation(inputRotation); // Set the rotator rotation from the user's input argument.
					
					try{
						// Create a reader for the input file and a writer for the output file.
						br = new BufferedReader(new FileReader(inputFile));
						bw = new BufferedWriter(new FileWriter(outputFile));

						readString = br.readLine(); // Read the first line and store it.
						
						// Check if the file actually contains data.
						if(readString == null){
							System.out.println("The supplied file does not contain any data");
						} else{
							// Itterate over the file's lines, rotate them and write them to the output file.
							for (boolean firstString = true, lastString = (readString == null); !lastString; firstString = false, readString = nextString) {
								lastString = ((nextString = br.readLine()) == null);
								
				                if(!firstString){
				                	bw.newLine();
				                }
				                
								writeString = rotator.rotate(readString, false);
								bw.write(writeString);
								
								// System.out.println("Input: " +readString);
								// System.out.println("Output: " +writeString);
				            }
						}
						
			        } catch(IOException e){
			        	// Catch any exception that might appear while reading and writing.
						e.printStackTrace();
					}finally {
						// Close all I/O streams. Catch exceptions here as well.
			        	if(br != null) try{br.close();bw.close();} catch(IOException e){e.printStackTrace();}
			        }
				} else{
					System.out.println("The supplied file is not valid");
				}
			} catch(NullPointerException e){
				System.out.println("The path supplied is not valid");
			}
		
		} else{
			// This part is run if there aren't any user arguments. It initilizes the GUI part of the program.  
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					Program program = new Program();
					program.init();
				}
			});	
		}
	}

	public void init(){
		// Create a new rotator and setup of the two main JFrames.
		rotator = new Rotator();
		initMainFrame();
		initAboutFrame();
	}

	public void initMainFrame(){
		// JFrame creation and component assignment. This part is pretty straight forward.
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
		rotationArrayLabel = new JLabel(
				"Rotation array (Seperate rotations with a comma)");
		rotationArrayLabel.setEnabled(false);

		inputArea = new JTextArea("Rotate me!", 10, 52);
		inputArea.setWrapStyleWord(true);
		inputArea.setLineWrap(true);
		inputArea.setCaretPosition(inputArea.getText().length());

		outputArea = new JTextArea("", 10, 52);
		outputArea.setWrapStyleWord(true);
		outputArea.setLineWrap(true);

		JScrollPane inputScrollPane = new JScrollPane(inputArea);
		JScrollPane outputScrollPane = new JScrollPane(outputArea);

		NumberFormat digitformat = NumberFormat.getInstance();
		digitformat.setMinimumIntegerDigits(1);
		digitformat.setMaximumIntegerDigits(2);

		rotField = new JFormattedTextField(digitformat);
		rotField.setValue((int) 13);
		rotField.setColumns(2);

		rotArrayField = new JFormattedTextField();
		rotArrayField.setText(rotator.getRotation());
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
		panel.add(Box.createRigidArea(new Dimension(640, 5)));
		panel.add(helpButton);
		panel.add(rotateButton);
		panel.add(unrotateButton);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(rotationLabel);
		panel.add(rotField);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(useArrayCheckbox);
		panel.add(Box.createRigidArea(new Dimension(640, 5)));
		panel.add(rotationArrayLabel);
		panel.add(rotArrayField);

		mainFrame.add(panel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public void initAboutFrame(){
		// This part initializes the JFrame used for the about window.
		aboutFrame = new JFrame("Rotcrypt - About");
		aboutFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		aboutFrame.setSize(new Dimension(480, 440));
		aboutFrame.setPreferredSize(new Dimension(480, 440));
		aboutFrame.setResizable(false);
		aboutFrame.setLocationRelativeTo(null);
		aboutFrame.setVisible(false);
		aboutFrame.addWindowListener(this);

		JPanel panel = new JPanel();

		String infoString = ("Using Rotcrypt is quite simple. Just enter something in the 'normal text' field and press the 'normal to rotated' button."
				+ " Depending on your designated amount of rotations in the rotations box, each letters alphabetical index will be moved by that amount causing the letter to change.\n\n"
				+ "As an example: Entering 'abcd' in the 'normal text' field and setting the 'rotations' to one will output 'bcde'."
				+ " The reason for this is that every letters index is moved by one space:\n\n"
				+ "INDEX:     1234   ---> 2345\n"
				+ "LETTER:  ABCD ---> BCDE\n\n"
				+ " You can now reverse the process if you press the 'rotated to normal' button. To do this however you"
				+ " will need to have the original number of rotations that were specified when the text was originally rotated.\n\n"
				+ "The check box 'use rotation array' can be used to specify a sequence of rotations that make it even harder to"
				+ " revert the text back to its original when the sequence is unknown. This can give your text another layer of"
				+ " protection due to the fact that more information is needed to remove the obfuscation from the text.\n");

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

		panel.add(Box.createRigidArea(new Dimension(640, 10)));
		panel.add(infoTextScrollPane);
		panel.add(Box.createRigidArea(new Dimension(640, 1)));
		panel.add(creditLabel);
		panel.add(urlLabel);
		panel.add(Box.createRigidArea(new Dimension(640, 1)));
		panel.add(closeButton);

		aboutFrame.add(panel);
		aboutFrame.pack();
	}
	
	// Removes characters irrelevant to the rotator.
	public String filterNumbers(String s){
		return(s.replaceAll("[^0123456789,-]", ""));
	}
	
	// Callback function for the buttons within the JFrames.
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand() == "encrypt"){
			if(inputArea.getText().length() > 0){
				if(!arrayEnabled){
					rotField.setValue(Integer.parseInt(rotField.getText()) % 26);
					rotator.setRotation(rotField.getText());
					outputArea.replaceRange(rotator.rotate(inputArea.getText(), false), 0, outputArea.getText().length());
				} else{
					rotator.setRotation(rotArrayField.getText());
					outputArea.replaceRange(rotator.rotate(inputArea.getText(), false), 0, outputArea.getText().length());
				}
			}
		}

		else if(e.getActionCommand() == "decrypt"){
			if(outputArea.getText().length() > 0){
				if(!arrayEnabled){
					rotField.setValue(Integer.parseInt(rotField.getText()) % 26);
					rotator.setRotation(rotField.getText());
					inputArea.replaceRange(rotator.rotate(outputArea.getText(), true), 0, inputArea.getText().length());
				} else{
					rotator.setRotation(rotArrayField.getText());
					inputArea.replaceRange(rotator.rotate(outputArea.getText(), true), 0, inputArea.getText().length());
				}
			}
		}
		
		// Makes the about window visible.
		else if(e.getActionCommand() == "about"){
			if(!aboutFrame.isVisible()){
				aboutFrame.setLocationRelativeTo(null);
				aboutFrame.setVisible(true);
			} else{
				aboutFrame.requestFocus();
			}
		}
		
		// Closes the about window.
		else if(e.getActionCommand() == "closeAbout"){
			aboutFrame.dispose();
			mainFrame.requestFocus();
		}
		
		// Rotation array checkbox.
		else if(e.getActionCommand() == "checkbox"){
			if(!arrayEnabled){
				arrayEnabled = true;
				rotationLabel.setEnabled(false);
				rotField.setEnabled(false);
				rotArrayField.setEnabled(true);
				rotationArrayLabel.setEnabled(true);
			} else{
				arrayEnabled = false;
				rotationLabel.setEnabled(true);
				rotField.setEnabled(true);
				rotationArrayLabel.setEnabled(false);
				rotArrayField.setEnabled(false);
			}
		}
	}
	
	// Filters the numbers within the rotation array field on focus and unfocus.
	public void focusGained(FocusEvent e){
		if(e.getSource() == rotArrayField) rotArrayField.setText(filterNumbers(rotArrayField.getText()));
	}

	public void focusLost(FocusEvent e){
		if(e.getSource() == rotArrayField) rotArrayField.setText(filterNumbers(rotArrayField.getText()));
	}

	public void windowActivated(WindowEvent e){

	}

	public void windowClosed(WindowEvent e){

	}
	
	// Closes the about frame's resources and focuses the main JFrame again.
	public void windowClosing(WindowEvent e){
		if(e.getSource() == aboutFrame){
			aboutFrame.dispose();
			mainFrame.requestFocus();
		}
	}

	public void windowDeactivated(WindowEvent e){

	}

	public void windowDeiconified(WindowEvent e){

	}

	public void windowIconified(WindowEvent e){

	}

	public void windowOpened(WindowEvent e){

	}
	
	// This event is mainly used for the URL label of the about frame.
	public void mouseClicked(MouseEvent e){
		if(e.getSource() == urlLabel){
			String url = "https://twitter.com/catlinman_";

			try{
				Desktop.getDesktop().browse(URI.create(url));
			} catch(IOException e2){
				e2.printStackTrace();
			}
		}
	}

	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}
}