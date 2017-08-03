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



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import ctrl.Converter;



public class GUIControl implements ActionListener
{
	//local instance variables
	ConverterGUI gui;
	
	
	//generic constructor
	public GUIControl()
	{
		gui = new ConverterGUI(this);
	}

	
	@Override
	//respond to user events
	public void actionPerformed(ActionEvent ae) 
	{
		switch(ae.getActionCommand())
		{
			//attempt to convert
			case(ConverterGUI.BTN_CONVERT):
			{
				if(!gui.getInputPath().isEmpty() && !gui.getOutputPath().isEmpty())
				{
					try
					{
						//read file
						String cfg = new String();
						Scanner scanner = new Scanner(new File(gui.getInputPath()));
						cfg = scanner.useDelimiter("\\Z").next();
						scanner.close();
			
						//convert
						byte[] bytes = Converter.cfgToBin(cfg);
						
						//write
						FileOutputStream output = new FileOutputStream(gui.getOutputPath(),false);
						output.write(bytes);
						output.close();
						gui.displayInfo("Conversion complete!");
					}
					catch (Exception e)
					{
						gui.displayError(e.getMessage());
					}
				}
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
}
