package net.sf.l2j.gameserver.ai2;

import java.util.List;

import javolution.util.FastList;
import net.sf.l2j.gameserver.TaskPriority;

/**
 * @author -Wooden-
 */
public class EventHandlerSet implements Comparable<EventHandlerSet>
{
	private int _comparatorPrio;
	private long _insertionTime;
	private final List<EventHandler> _handlers;
	private final AiEventType _eventType;
	
	public EventHandlerSet(AiEventType event, List<EventHandler> handlers, TaskPriority prio)
	{
		_comparatorPrio = (prio.ordinal() + 1) * 3;
		_handlers = new FastList<>();
		_eventType = event;
		for (EventHandler handler : handlers)
		{
			addHandler(handler);
		}
	}
	
	public EventHandlerSet(EventHandler handler, TaskPriority prio)
	{
		_comparatorPrio = (prio.ordinal() + 1) * 3;
		_handlers = new FastList<>();
		_eventType = handler.getEvenType();
		addHandler(handler);
	}
	
	public void addHandler(EventHandler handler)
	{
		if (handler == null)
		{
			return;
		}
		int prio = handler.getPriority();
		int index = -1;
		for (EventHandler eventHandler : _handlers)
		{
			if (eventHandler.getPriority() <= prio)
			{
				index = eventHandler.getPriority();
				break;
			}
		}
		if (index != -1)
		{
			_handlers.add(index, handler);
		}
		else
		{
			_handlers.add(handler);
		}
	}
	
	public void setPrio(TaskPriority prio)
	{
		_comparatorPrio = (prio.ordinal() + 1) * 3;
	}
	
	public void stampInsertionTime()
	{
		_insertionTime = System.currentTimeMillis();
	}
	
	public int getComparatorPriority()
	{
		return _comparatorPrio;
	}
	
	public List<EventHandler> getHandlers()
	{
		return _handlers;
	}
	
	public AiEventType getEventType()
	{
		return _eventType;
	}
	
	@Override
	public int compareTo(EventHandlerSet es)
	{
		return ((int) ((System.currentTimeMillis() - _insertionTime) / 1000) + _comparatorPrio) - es.getComparatorPriority();
	}
	
	@Override
	public String toString()
	{
		String str = "EventHandlerSet: size:" + _handlers.size() + " Priority:" + _comparatorPrio + (_insertionTime != 0 ? " TimePoints: " + (int) ((System.currentTimeMillis() - _insertionTime) / 1000) : "");
		for (EventHandler handler : _handlers)
		{
			str = str.concat(" - " + handler.toString());
		}
		return str;
	}
}