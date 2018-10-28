/**
*Class:             Converter.java
*Project:          	TI .cfg to .bin Converter
*Author:            Jason Van Kerkhoven                                             
*Date of Update:    18/07/2017                                              
*Version:           1.0.0                                         
*                                                                                   
*Purpose:          	Algorithm for converting Texas Instrument's .cfg file for a 
*					DSP, generated from the coefficient tool, into a usable binary
*					file for the actual DSP in use.
*					
* 
*Update Log			v1.0.0
*						- null
*/
package ctrl;




import java.io.ByteArrayOutputStream;




public class Converter 
{
	//declaring class constants
	private static final String COEFFICIENT = "w";
	private static final String COMMENT = "#";
	private static final String EXCEPTION = "> 00";
	private static final byte FILE_START = (byte)0xE0;
	
	private static final String BASE = "allo-piano-dsp";
	
	
	//generic constructor
	private Converter() {}
	
	
	//convert Texas Instruments .cfg file to compatible .bin file
	public static byte[] cfgToBin(String cfg) throws FileFormatException
	{
		//create stream to hold bytes
		ByteArrayOutputStream bin = new ByteArrayOutputStream();
		bin.write(FILE_START);
		
		//process and convert
		String[] lines = cfg.split("\r\n");
		for (int i=0; i < lines.length; i++)
		{
			if (lines[i].startsWith(COEFFICIENT))
			{
				String[] bytes = lines[i].substring(2).split(" ");		//this assumes there is ALWAYS a space after the w
				if (bytes.length == 3)
				{
					for (String b : bytes)
					{
						if (b.length() == 2)
						{
							byte value = 0;
							for (int a=0; a<2; a++)
							{
								byte nibble = (byte)b.charAt(a);
								if (nibble >=  0x30 && nibble <= 0x39)
								{
									nibble = (byte)(nibble - 0x30);
								}
								else if (nibble >= 0x61 && nibble <= 0x66)
								{
									nibble = (byte)(nibble - 0x57);
								}
								else
								{
									throw new FileFormatException("line: " + (i+1) + " \"" + lines[i] + "\" -- value is out of hex range");
								}
								
								if(a == 0)
								{
									value += (nibble) << 4;
								}
								else
								{
									value += nibble;
								}
							}
							bin.write(value);
						}
						else
						{
							throw new FileFormatException("line: " + (i+1) + " \"" + lines[i] +  "\" -- a single value must be 2 hex digits wide");
						}
					}
				}
				else
				{
					throw new FileFormatException("line: " + (i+1) + " \"" + lines[i] + "\" -- lines starting with 'w' must have 3 2-digit values seperated by a space");
				}
			}
			else if (lines[i].equals(EXCEPTION))
			{
				bin.write(0x98);
				bin.write(0x01);
				bin.write(0x00);
			}
			else if (!lines[i].startsWith(COMMENT) && !lines[i].isEmpty())
			{
				throw new FileFormatException("Line: " + (i+1) + " Unknown line format -- \"" + lines[i] + "\"");
			}
		}
		
		//return byte array from bin
		return bin.toByteArray();
	}

	
	//convert file name from TI .cfg to Volumio-readable .bin file
	public static String convertFileName(String cfg) throws FileFormatException
	{
		// base_xo60_Rate44.cfg
		if (cfg.matches("(\\w+_\\w+\\d_\\w+\\d.cfg)"))
		{
			//split into 3 values
			String[] tmp = cfg.split("_");
			if (tmp.length != 3)
			{
				throw new FileFormatException("Input file name must be of format base_labelnnn_Ratennn.cfg");
			}
			
			//parse
			String base = BASE;
			String label = tmp[1];
			String rate = tmp[2].split("\\.")[0];
			
			//process label
			label = label.replaceAll("\\D", "");
			if (label.length() > 5 || label.length() < 2)
			{
				throw new FileFormatException("Label numeric suffix must be between (inclusive) 2 and 5 digits");
			}
			label = label + "-0";
			
			//process rate
			switch (rate)
			{
				case("Rate44"):
					rate = "44100";
					break;
				case("Rate48"):
					rate = "48000";
					break;
				case("Rate88"):
					rate = "88200";
					break;
				case("Rate96"):
					rate = "96000";
					break;
				case("Rate176"):
					rate = "176400";
					break;
				case("Rate192"):
					rate = "192000";
					break;
				default:
					throw new FileFormatException("Unsupported rate value -- must be in list {Rate44,Rate48,Rate88,Rate96,Rate176,Rate192}");
			}
			
			//return valid formated bin name
			return base+"-"+rate+"-"+label+".bin";
		}
		else
		{
			throw new FileFormatException("Input file name must be of format base_labelnnn_Ratennn");
		}
	}
}
