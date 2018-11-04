/**
*Class:             GUIControl.java
*Project:          	TI .cfg to .bin Converter
*Author:            Jason Van Kerkhoven                                             
*Date of Update:    18/07/2017                                              
*Version:           1.0.0                                         
*                                                                                   
*Purpose:          	Control for GUI. Mostly listens for a button to be pressed.
*					Then runs the converter method.
*					
* 
*Update Log			v1.0.0
*						- null
*/
package ui;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

import ctrl.Converter;
import ctrl.FileFormatException;



public class GUIControl implements ActionListener
{
	//local instance variables
	ConverterGUI gui;
	
	
	//generic constructor
	public GUIControl() throws BadLocationException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		gui = new ConverterGUI(this);
	}
	
	
	//convert a single file
	private void convert(String in, String out) throws IOException, FileFormatException
	{
		//read file
		String cfg = new String();
		Scanner scanner = new Scanner(new File(in));
		cfg = scanner.useDelimiter("\\Z").next();
		scanner.close();

		//convert
		byte[] bytes = Converter.cfgToBin(cfg);
		
		//write file
		FileOutputStream output = new FileOutputStream(out,false);
		output.write(bytes);
		output.close();
	}
	
	
	@Override
	//respond to user events
	public void actionPerformed(ActionEvent ae) 
	{
		try
		{
			switch(ae.getActionCommand())
			{
				//attempt to convert
				case(ConverterGUI.BTN_CONVERT):
				{
					if(!gui.getInputPath().isEmpty() && !gui.getOutputPath().isEmpty())
					{
						//determine operation mode
						String inPath = gui.getInputPath();
						String outPath = gui.getOutputPath();
						File in = new File(inPath);
						File out = new File(outPath);
						
						//convert directory to directory
						if (in.isDirectory() && out.isDirectory())
						{
							//prepare instance variables
							File[] allFiles = in.listFiles();
							int fail = 0;
							int total = 0;
							gui.println("Scanning source directory at " + inPath + " ...");
							
							//scan all files, try to convert any .cfg files
							gui.setProgressBounds(0, allFiles.length);
							gui.setProgress(0);
							gui.showProgressText(true);
							
							
							int i = 0;
							for (File file : allFiles)
							{
								try
								{
									if (file.getName().matches(".+(.cfg)"))
									{
										total++;
										String outName = Converter.convertFileName(file.getName());
										String pathToWrite = (outPath.concat("\\" + outName));
										convert(file.getAbsolutePath(), pathToWrite);
										gui.println("[SUCCESS] " + file.getName() + " --> " + outName);
									}
								}
								catch (FileFormatException e)
								{
									gui.println("[FAILURE] " + file.getName() + ": " + e.getMessage());
									fail++;
								}
								
								i++;
								gui.setProgress(i);
							}
							
							//print summary
							gui.println("------------------------------");
							gui.println("TOTAL FILES:  " + total);
							gui.println("SUCCESSES:    "  + (total-fail));
							gui.println("FAILURES:     " + fail);
							gui.println("------------------------------");
							gui.println("");
						}
						//convert file to file
						else if (in.isFile() && (out.isFile() || !out.exists()))
						{
							try
							{
								convert(in.getAbsolutePath(), out.getAbsolutePath());
								gui.println("[SUCCESS] " + in.getName() + " --> " + out.getName());
								gui.println("");
								gui.displayInfo(in.getName() + " converted to " + out.getName());
							}
							catch (FileFormatException e)
							{
								gui.println("[FAILURE] " + in.getName() + " --> " + out.getName());
								gui.println(e.getMessage());
								gui.println("");
								gui.displayError(e.getMessage());
							}
						}	
						//type mismatch error
						else
						{
							String inType = in.isDirectory()?"DIRECTORY":"FILE";
							String outType = out.isDirectory()?"DIRECTORY":"FILE";
							gui.displayError("Type mismatch\nInput given as " + inType + ", Output give as " + outType + "\nInput and Output must both be valid files OR directories");
						}
					}
					//valid path needed 
					else
					{
						gui.displayError("A valid input and output must be selected.");
					}
					break;
				}
				
				//exit program
				case(ConverterGUI.BTN_CANCEL):
				{
					System.exit(0);
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}
}
