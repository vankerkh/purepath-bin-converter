/**
*Class:             FileFormatExcepion.java
*Project:          	TI .cfg to .bin Converter
*Author:            Jason Van Kerkhoven                                             
*Date of Update:    18/07/2017                                              
*Version:           1.0.0                                         
*                                                                                   
*Purpose:          	Generic Java exception.
*					
* 
*Update Log			v1.0.0
*						- null
*/
package ctrl;




public class FileFormatException extends Exception 
{
	//generic constructor
	public FileFormatException(String msg)
	{
		super(msg);
	}
}
