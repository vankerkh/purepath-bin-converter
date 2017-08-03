/**
*Class:             ConverterGUI.java
*Project:          	TI .cfg to .bin Converter
*Author:            Jason Van Kerkhoven                                             
*Date of Update:    18/07/2017                                              
*Version:           1.0.0                                         
*                                                                                   
*Purpose:          	GUI for use of converter program without command line/terminal.
*					
* 
*Update Log			v1.0.0
*						- null
*/
package ui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Font;



public class ConverterGUI extends JFrame implements ActionListener
{
	//declaring global static constants
	public static final String BTN_CONVERT = "btn/convert";
	public static final String BTN_CANCEL = "btn/cancel";
	
	//declaring local static constants
	private static final String WINDOW_NAME = "TI .cfg to .bin Converter";
	private static final String BTN_INPUT = "btn/input";
	private static final String BTN_OUTPUT = "btn/output";
	
	//declaring local instance variables
	private JTextField inputTxt;
	private JTextField outputTxt;
	private JButton btnConvert;

	
	//generic constructor
	public ConverterGUI(ActionListener listener)
	{
		//set frame properties
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		this.setSize(425, 275);
		this.setLocationRelativeTo(null);
		this.setTitle(WINDOW_NAME);
		
		//add input path text input/display
		inputTxt = new JTextField();
		inputTxt.setFont(new Font("Tahoma", Font.PLAIN, 22));
		inputTxt.setColumns(10);
		inputTxt.setBounds(10, 42, 300, 60);
		getContentPane().add(inputTxt);
		
		//add output path text input/display
		outputTxt = new JTextField();
		outputTxt.setFont(new Font("Tahoma", Font.PLAIN, 22));
		outputTxt.setColumns(10);
		outputTxt.setBounds(10, 113, 300, 60);
		getContentPane().add(outputTxt);
		
		//add button to launch dialog for input selection
		JButton btnInput = new JButton("Input");
		btnInput.setBounds(320, 52, 89, 41);
		btnInput.addActionListener(this);
		btnInput.setActionCommand(BTN_INPUT);
		getContentPane().add(btnInput);
		
		//add button to launch dialog for output selection
		JButton btnOutput = new JButton("Output");
		btnOutput.setBounds(320, 121, 89, 41);
		btnOutput.addActionListener(this);
		btnOutput.setActionCommand(BTN_OUTPUT);
		getContentPane().add(btnOutput);
		
		//add button to cancel
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(239, 207, 170, 23);
		btnCancel.addActionListener(listener);
		btnCancel.setActionCommand(BTN_CANCEL);
		getContentPane().add(btnCancel);
		
		//add button to convert
		btnConvert = new JButton("Convert");
		btnConvert.setBounds(10, 207, 170, 23);
		btnConvert.addActionListener(listener);
		btnConvert.setActionCommand(BTN_CONVERT);
		getContentPane().add(btnConvert);
		
		this.setVisible(true);
	}
	
	
	//set input path
	private void setInput(String path)
	{
		this.inputTxt.setText(path);
	}
	
	
	//set output path
	private void setOutput(String path)
	{
		this.outputTxt.setText(path);
	}

	
	//generic getters
	public String getInputPath()
	{
		return inputTxt.getText();
	}
	public String getOutputPath()
	{
		return outputTxt.getText();
	}
	
	
	//display error
	public void displayError(String msg)
	{
		JOptionPane.showMessageDialog(this, msg, WINDOW_NAME, JOptionPane.ERROR_MESSAGE);
	}
	
	
	//display info
	public void displayInfo(String msg)
	{
		JOptionPane.showMessageDialog(this, msg, WINDOW_NAME, JOptionPane.INFORMATION_MESSAGE);
	}


	@Override
	//respond to path buttons
	public void actionPerformed(ActionEvent e) 
	{
		switch (e.getActionCommand())
		{
			//set input path
			case(BTN_INPUT):
			{
				//open a a file chooser
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(this);
				
				//check if user selected
				File file = fc.getSelectedFile();
				if (file != null)
				{
					this.setInput(file.getAbsolutePath());
					this.outputTxt.setText(file.getAbsolutePath().split("\\.")[0].concat(".bin"));
				}
				break;
			}
				
				
			//set output path
			case(BTN_OUTPUT):
			{
				//open a a file chooser
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(this);
				
				//check if user selected
				File file = fc.getSelectedFile();
				if (file != null)
				{
					this.setOutput(file.getAbsolutePath());
				}
				break;
			}
		}
	}
}
