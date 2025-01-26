package net.sf.l2j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileLogFormatter extends Formatter
{
	private static final String CRLF = "\r\n";
	private static final String TAB = "\t";
	private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss,SSS");
	
	@Override
	public String format(LogRecord record)
	{
		StringBuilder output = new StringBuilder();
		
		return output.append(dateFmt.format(new Date(record.getMillis())))
			.append(TAB)
			.append(record.getLevel().getName())
			.append(TAB)
			.append(record.getLongThreadID())
			.append(TAB)
			.append(record.getLoggerName())
			.append(TAB)
			.append(record.getMessage())
			.append(CRLF)
			.toString();
	}
}