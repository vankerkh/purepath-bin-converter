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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ctrl.Converter;
import ctrl.FileFormatException;

import javax.swing.SwingConstants;



public class ConverterGUI extends JFrame implements ActionListener
{
	//declaring global static constants
	public static final String BTN_CONVERT = "btn/convert";
	public static final String BTN_CANCEL = "btn/cancel";
	
	//declaring local static constants
	private static final String WINDOW_NAME = "TI .cfg to .bin Converter";
	private static final String BTN_INPUT = "btn/input";
	private static final String BTN_OUTPUT = "btn/output";
	private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 11);
	private static final Font TEXT_FIELD_FONT = new Font("Tahoma", Font.PLAIN, 14);
	private static final Font LOG_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	
	//declaring local instance variables
	private JTextField labelInput;
	private JTextField labelOutput;
	private JTextField inputTxt;
	private JTextField outputTxt;
	private JButton btnConvert;
	private JProgressBar progressBar;
	private JTextArea log;

	
	//generic constructor
	public ConverterGUI(ActionListener listener) throws BadLocationException
	{
		//set frame properties
		this.setResizable(false);
		this.setSize(800, 500);
		this.setLocationRelativeTo(null);
		this.setTitle(WINDOW_NAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container mainframe = this.getContentPane();
		mainframe.setLayout(new BorderLayout(5,5));
		
		
		//setup controller panel
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(350,0));
		mainframe.add(controlPanel, BorderLayout.WEST);
		controlPanel.setVisible(true);
		controlPanel.setLayout(null);
		
		
		//setup logging panel
		log = new JTextArea();
		log.setEditable(true);
		log.setFont(LOG_FONT);
		log.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(log);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mainframe.add(scrollPane, BorderLayout.CENTER);
		
		
		//add labels for input/output paths
		labelInput = new JTextField();
		labelInput.setFont(LABEL_FONT);
		labelInput.setEditable(false);
		labelInput.setBackground(null);
		labelInput.setBorder(null);
		labelInput.setHorizontalAlignment(SwingConstants.CENTER);
		labelInput.setText("Input:");
		labelInput.setBounds(10, 22, 330, 20);
		labelInput.setColumns(10);
		controlPanel.add(labelInput);
		
		labelOutput = new JTextField();
		labelOutput.setEditable(false);
		labelOutput.setBackground(null);
		labelOutput.setBorder(null);
		labelOutput.setText("Output:");
		labelOutput.setFont(LABEL_FONT);
		labelOutput.setHorizontalAlignment(SwingConstants.CENTER);
		labelOutput.setColumns(10);
		labelOutput.setBounds(10, 119, 330, 20);
		controlPanel.add(labelOutput);
		
		
		//add input path text input/display
		inputTxt = new JTextField();
		inputTxt.setLocation(10, 50);
		inputTxt.setSize(330, 30);
		inputTxt.setFont(TEXT_FIELD_FONT);
		inputTxt.setColumns(10);
		controlPanel.add(inputTxt);
		
		
		//add output path text input/display
		outputTxt = new JTextField();
		outputTxt.setLocation(10, 150);
		outputTxt.setSize(330, 30);
		outputTxt.setFont(TEXT_FIELD_FONT);
		outputTxt.setColumns(10);
		controlPanel.add(outputTxt);
		
		
		//add button to launch dialog for input selection
		JButton btnInput = new JButton("Select Input");
		btnInput.setLocation(10, 290);
		btnInput.setSize(155, 30);
		btnInput.addActionListener(this);
		btnInput.setActionCommand(BTN_INPUT);
		controlPanel.add(btnInput);
		
		
		//add button to launch dialog for output selection
		JButton btnOutput = new JButton("Select Output");
		btnOutput.setLocation(185, 290);
		btnOutput.setSize(155, 30);
		btnOutput.addActionListener(this);
		btnOutput.setActionCommand(BTN_OUTPUT);
		controlPanel.add(btnOutput);
		
		
		//add button to cancel
		JButton btnCancel = new JButton("Close");
		btnCancel.setLocation(185, 331);
		btnCancel.setSize(155, 30);
		btnCancel.addActionListener(listener);
		btnCancel.setActionCommand(BTN_CANCEL);
		controlPanel.add(btnCancel);
		
		
		//add button to convert
		btnConvert = new JButton("Convert");
		btnConvert.setLocation(10, 331);
		btnConvert.setSize(155, 30);
		btnConvert.addActionListener(listener);
		btnConvert.setActionCommand(BTN_CONVERT);
		controlPanel.add(btnConvert);
		
		
		//add progress bar
		progressBar = new JProgressBar();
		progressBar.setLocation(10, 402);
		progressBar.setSize(330, 30);
		progressBar.setForeground(Color.GREEN);
		progressBar.setBorderPainted(true);
		this.showProgressText(false);
		this.resetProgress();
		controlPanel.add(progressBar);
		
		this.setVisible(true);
	}
	
	
	//print regular line to log
	public void println(String s) throws BadLocationException
	{
		log.append(s+"\n");
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
	
	
	//set progress bar parameters
	public void setProgressBounds(int min, int max)
	{
		if (progressBar.getValue() > max)
		{
			progressBar.setValue(max);
		}
		else if (progressBar.getValue() < min)
		{
			progressBar.setValue(min);
		}
		progressBar.setMaximum(max);
		progressBar.setMinimum(min);	
	}
	
	
	//reset progress bar
	public void resetProgress()
	{
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
	}
	
	
	//set progress value
	public void setProgress(int n)
	{
		progressBar.setValue(n);
	}
	
	
	//enable progress status
	public void showProgressText(boolean flag)
	{
		progressBar.setStringPainted(flag);
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
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fc.showOpenDialog(this);
				
				//check if user selected
				File file = fc.getSelectedFile();
				if (file != null)
				{
					if (file.isDirectory())
					{
						this.setInput(file.getAbsolutePath());
						this.setOutput(file.getAbsolutePath());
						
						this.labelInput.setText("Input Directory:");
						this.labelOutput.setText("Output Directory:");
					}
					else if (file.isFile())
					{
						this.setInput(file.getAbsolutePath());
						try 
						{
							String name = Converter.convertFileName(file.getName());
							this.setOutput(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\")).concat("\\" + name));
						} 
						catch (FileFormatException ce)
						{
							this.setOutput(file.getAbsolutePath().split("\\.")[0].concat(".bin"));
						}

						this.labelInput.setText("Input File:");
						this.labelOutput.setText("Output File:");
					}
				}
				break;
			}
				
				
			//set output path
			case(BTN_OUTPUT):
			{
				//open a a file chooser
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fc.showOpenDialog(this);
				
				//check if user selected
				File file = fc.getSelectedFile();
				if (file != null)
				{
					if (file.isFile())
					{
						this.setOutput(file.getAbsolutePath());
						this.labelOutput.setText("Output File:");
					}
					else if (file.isDirectory())
					{
						this.setOutput(file.getAbsolutePath());
						this.labelOutput.setText("Output Directory:");
					}
				}
				break;
			}
		}
	}
}
