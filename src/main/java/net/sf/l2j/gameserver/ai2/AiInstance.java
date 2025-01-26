package net.sf.l2j.gameserver.ai2;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.TaskPriority;

/**
 * @author -Wooden-
 */
public class AiInstance
{
	private final Map<AiEventType, EventHandlerSet> _eventHandlers;
	private final AiPlugingParameters _pluginigParams;
	
	public AiInstance(AiPlugingParameters params)
	{
		if (params.isConverted())
		{
			throw new IllegalArgumentException("AiPluginingParameters of an Ai instance must be converted");
		}
		_pluginigParams = params;
		// TODO:update the params (bottom-up)
		_eventHandlers = new ConcurrentHashMap<>();
		AiManager.getInstance().addAiInstance(this);
	}
	
	public AiInstance(AiInstance instance, AiPlugingParameters params)
	{
		this(params);
		copyHanlders(instance);
	}
	
	public void copyHanlders(AiInstance instance)
	{
		// then copy all the hanlders from 'instance'
		for (EventHandlerSet set : instance.getEventHandlerSets())
		{
			addHandlerSet(set.getEventType(), set);
		}
	}
	
	/**
	 * <p>
	 * This methode add the handler to the {@link EventHandlerSet} associated with the specified{@link AiEventType}
	 * </p>
	 * @param handler the handler to be added
	 */
	public void addHandler(EventHandler handler)
	{
		EventHandlerSet set = _eventHandlers.get(handler.getEvenType());
		if (set == null)
		{
			set = new EventHandlerSet(handler, TaskPriority.PR_NORMAL);
			_eventHandlers.put(handler.getEvenType(), set);
		}
		else
		{
			set.addHandler(handler);
		}
	}
	
	public void addHandlerSet(AiEventType event, EventHandlerSet set)
	{
		_eventHandlers.put(event, set);
	}
	
	public class QueueEventRunner implements Runnable
	{
		
		private final EventHandlerSet _set;
		private final AiParameters _ai;
		private final AiEvent _event;
		
		public QueueEventRunner(EventHandlerSet set, AiParameters ai, AiEvent event)
		{
			_set = set;
			_ai = ai;
			_event = event;
		}
		
		@Override
		public void run()
		{
			for (EventHandler handler : _set.getHandlers())
			{
				handler.runImpl(_ai, _event);
			}
			launchNextEvent(_ai);
		}
	}
	
	/**
	 * @param aiParams
	 */
	public void launchNextEvent(AiParameters aiParams)
	{
		if (aiParams.hasEvents())
		{
			AiEvent event = aiParams.nextEvent();
			AiManager.getInstance().executeEventHandler(new QueueEventRunner(_eventHandlers.get(event.getType()), aiParams, event));
		}
	}
	
	public void triggerEvent(AiEvent event, AiParameters aiParams)
	{
		synchronized (aiParams)
		{
			boolean restart = false;
			// if there was no events in the queue start processing events after we add them.
			if (!aiParams.hasEvents())
			{
				restart = true;
			}
			aiParams.queueEvents(event);
			if (restart)
			{
				launchNextEvent(aiParams);
			}
		}
	}
	
	public AiPlugingParameters getPluginingParamaters()
	{
		return _pluginigParams;
	}
	
	public Collection<EventHandlerSet> getEventHandlerSets()
	{
		return _eventHandlers.values();
	}
	
	/**
	 * @return
	 */
	public Set<Integer> getHandledNPCIds()
	{
		return _pluginigParams.getIDs();
	}
	
}