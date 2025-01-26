package net.sf.l2j.gameserver.ai2;

import net.sf.l2j.gameserver.model.L2Character;

public class AiEvent
{
	private final AiEventType _type;
	private final L2Character _source;
	private final L2Character _target;
	
	public AiEvent(AiEventType type, L2Character source, L2Character target)
	{
		_type = type;
		_source = source;
		_target = target;
	}
	
	public AiEventType getType()
	{
		return _type;
	}
	
	public L2Character getSource()
	{
		return _source;
	}
	
	public L2Character getTarget()
	{
		return _target;
	}
}