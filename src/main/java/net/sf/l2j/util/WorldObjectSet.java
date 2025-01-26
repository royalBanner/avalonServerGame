package net.sf.l2j.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.model.L2Object;

public class WorldObjectSet<T extends L2Object> extends L2ObjectSet<T>
{
	private final Map<Integer, T> _objectMap;
	
	public WorldObjectSet()
	{
		_objectMap = new ConcurrentHashMap<Integer, T>();
	}
	
	@Override
	public int size()
	{
		return _objectMap.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return _objectMap.isEmpty();
	}
	
	@Override
	public void clear()
	{
		_objectMap.clear();
	}
	
	@Override
	public void put(T obj)
	{
		_objectMap.put(obj.getObjectId(), obj);
	}
	
	@Override
	public void remove(T obj)
	{
		_objectMap.remove(obj.getObjectId());
	}
	
	@Override
	public boolean contains(T obj)
	{
		return _objectMap.containsKey(obj.getObjectId());
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return _objectMap.values().iterator();
	}
}