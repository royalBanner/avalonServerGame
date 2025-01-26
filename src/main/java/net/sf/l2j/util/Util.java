package net.sf.l2j.util;

public class Util
{
	public static boolean isInternalIP(String ipAddress)
	{
		return (ipAddress.startsWith("192.168.") || ipAddress.startsWith("10.") ||
		// ipAddress.startsWith("172.16.") ||
		// Removed because there are some net IPs in this range.
		// TODO: Use regexp or something to only include 172.16.0.0 => 172.16.31.255
		ipAddress.startsWith("127.0.0.1"));
	}
	
	public static String printData(byte[] data, int len)
	{
		StringBuilder result = new StringBuilder();
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if ((counter % 16) == 0)
			{
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					int t1 = data[charpoint++];
					if ((t1 > 0x1f) && (t1 < 0x80))
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append("\n");
				counter = 0;
			}
		}
		
		int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < (17 - rest); i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				int t1 = data[charpoint++];
				if ((t1 > 0x1f) && (t1 < 0x80))
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append("\n");
		}
		
		return result.toString();
	}
	
	public static String fillHex(int data, int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i = number.length(); i < digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
	
	public static String printData(byte[] raw)
	{
		return printData(raw, raw.length);
	}
}