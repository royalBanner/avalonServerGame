package net.sf.l2j;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * @author Advi
 */
public class ItemFilter implements Filter
{
	private String _excludeProcess;
	private String _excludeItemType;
	
	// This is example how to exclude consuming of shots and arrows from logging
	// private String _excludeProcess = "Consume";
	// private String _excludeItemType = "Arrow, Shot";
	
	@Override
	public boolean isLoggable(LogRecord record)
	{
		if (record.getLoggerName() != "item")
		{
			return false;
		}
		if (_excludeProcess != null)
		{
			// if (record.getMessage() == null) return true;
			String[] messageList = record.getMessage().split(":");
			if ((messageList.length < 2) || !_excludeProcess.contains(messageList[1]))
			{
				return true;
			}
		}
		if (_excludeItemType != null)
		{
			// if (record.getParameters() == null || record.getParameters().length == 0 || !(record.getParameters()[0] instanceof L2ItemInstance)) return true;
			L2ItemInstance item = ((L2ItemInstance) record.getParameters()[0]);
			if (!_excludeItemType.contains(item.getItemType().toString()))
			{
				return true;
			}
		}
		return ((_excludeProcess == null) && (_excludeItemType == null));
	}
}