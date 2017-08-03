/**
*Class:             Main.java
*Project:          	TI .cfg to .bin Converter
*Author:            Jason Van Kerkhoven                                             
*Date of Update:    18/07/2017                                              
*Version:           1.0.0                                         
*                                                                                   
*Purpose:          	What it says on the tin.
*					
* 
*Update Log			v1.0.0
*						- null
*/
package ctrl;



import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import ui.GUIControl;




public class Main 
{
	public static void main (String[] args)
	{
		if (args.length == 1)
		{
			try
			{
				//read file
				String path = args[0];
				String cfg = new String();
				Scanner scanner = new Scanner(new File(path));
				cfg = scanner.useDelimiter("\\Z").next();
				scanner.close();
	
				//convert
				System.out.println("Converting...");
				byte[] bytes = Converter.cfgToBin(cfg);
				
				//write
				FileOutputStream output = new FileOutputStream(path.split("\\.")[0] + ".bin",false);
				output.write(bytes);
				output.close();
				System.out.println("Conversion complete!");
			}
			catch (Exception e)
			{
				System.out.println("ERROR!  " + e.getMessage());
			}
		}
		else
		{
			GUIControl gui = new GUIControl();
		}
	}
}
