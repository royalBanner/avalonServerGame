package net.sf.l2j.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.model.L2Object;

public class WorldObjectMap<T extends L2Object> extends L2ObjectMap<T>
{
	private final Map<Integer, T> _objectMap = new ConcurrentHashMap<Integer, T>();
	
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
		if (obj != null)
		{
			_objectMap.put(obj.getObjectId(), obj);
		}
	}
	
	@Override
	public void remove(T obj)
	{
		if (obj != null)
		{
			_objectMap.remove(obj.getObjectId());
		}
	}
	
	@Override
	public T get(int id)
	{
		return _objectMap.get(id);
	}
	
	@Override
	public boolean contains(T obj)
	{
		if (obj == null)
		{
			return false;
		}
		return _objectMap.get(obj.getObjectId()) != null;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return _objectMap.values().iterator();
	}
}