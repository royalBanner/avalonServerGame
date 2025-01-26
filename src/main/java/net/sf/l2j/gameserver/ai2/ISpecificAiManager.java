package net.sf.l2j.gameserver.ai2;

import java.util.List;

/**
 * @author -Wooden-
 */
public interface ISpecificAiManager
{
	List<EventHandler> getEventHandlers();
	
	void load();
}