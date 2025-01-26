package net.sf.l2j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * @author Advi
 */
public class ItemLogFormatter extends Formatter
{
	private static final String CRLF = "\r\n";
	private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd MMM H:mm:ss");
	
	@Override
	public String format(LogRecord record)
	{
		StringBuilder output = new StringBuilder();
		output.append('[');
		output.append(dateFmt.format(new Date(record.getMillis())));
		output.append(']');
		output.append(' ');
		output.append(record.getMessage());
		for (Object p : record.getParameters())
		{
			if (p == null)
			{
				continue;
			}
			output.append(',');
			output.append(' ');
			if (p instanceof L2ItemInstance)
			{
				L2ItemInstance item = (L2ItemInstance) p;
				output.append("item " + item.getObjectId() + ":");
				if (item.getEnchantLevel() > 0)
				{
					output.append("+" + item.getEnchantLevel() + " ");
				}
				output.append(item.getItem().getName());
				output.append("(" + item.getCount() + ")");
			}
			else
			{
				output.append(p.toString());
			}
		}
		output.append(CRLF);
		
		return output.toString();
	}
}