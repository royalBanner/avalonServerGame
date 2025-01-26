package net.sf.l2j.gameserver.ai2;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import javolution.util.FastList;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;

/**
 * @author -Wooden-
 */
public class AiParameters
{
	private final Queue<AiEvent> _eventQueue;
	private final L2NpcInstance _actor;
	private final List<Hated> _hated;
	private final List<Liked> _liked;
	
	public class Hated
	{
		public L2Character character;
		public HateReason reason;
		public int degree;
		
	}
	
	public class Liked
	{
		public L2Character character;
		public LikeReason reason;
		public int degree;
	}
	
	public enum HateReason
	{
		GAVE_DAMMAGE,
		HEALS_ENNEMY,
		GAVE_DAMMAGE_TO_FRIEND,
		IS_ENNEMY
	}
	
	public enum LikeReason
	{
		FRIEND,
		HEALED,
		HEALED_FRIEND,
		GAVE_DAMMAGE_TO_ENNEMY
	}
	
	public AiParameters(L2NpcInstance actor)
	{
		_eventQueue = new PriorityBlockingQueue<>();
		_hated = new FastList<>();
		_liked = new FastList<>();
		_actor = actor;
	}
	
	/**
	 * @return
	 */
	public boolean hasEvents()
	{
		return _eventQueue.isEmpty();
	}
	
	/**
	 * @return
	 */
	public AiEvent nextEvent()
	{
		return _eventQueue.poll();
	}
	
	public void queueEvents(AiEvent set)
	{
		_eventQueue.offer(set);
	}
	
	public L2NpcInstance getActor()
	{
		return _actor;
	}
	
	public List<Hated> getHated()
	{
		return _hated;
	}
	
	public List<Liked> getLiked()
	{
		return _liked;
	}
	
	public void addLiked(Liked cha)
	{
		_liked.add(cha);
	}
	
	public void addHated(Hated cha)
	{
		_hated.add(cha);
	}
	
	public void clear()
	{
		_hated.clear();
		_liked.clear();
		_eventQueue.clear();
	}
	
}