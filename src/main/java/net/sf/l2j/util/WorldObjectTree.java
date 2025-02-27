package net.sf.l2j.util;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.l2j.gameserver.model.L2Object;

/**
 * @author dishkols
 * @param <T>
 */
public class WorldObjectTree<T extends L2Object> extends L2ObjectMap<T>
{
	private final TreeMap<Integer, T> _objectMap = new TreeMap<>();
	private final ReentrantReadWriteLock _rwl = new ReentrantReadWriteLock();
	private final Lock _r = _rwl.readLock();
	private final Lock _w = _rwl.writeLock();
	
	@Override
	public int size()
	{
		_r.lock();
		try
		{
			return _objectMap.size();
		}
		finally
		{
			_r.unlock();
		}
	}
	
	@Override
	public boolean isEmpty()
	{
		_r.lock();
		try
		{
			return _objectMap.isEmpty();
		}
		finally
		{
			_r.unlock();
		}
	}
	
	@Override
	public void clear()
	{
		_w.lock();
		try
		{
			_objectMap.clear();
		}
		finally
		{
			_w.unlock();
		}
	}
	
	@Override
	public void put(T obj)
	{
		if (obj != null)
		{
			_w.lock();
			try
			{
				_objectMap.put(obj.getObjectId(), obj);
			}
			finally
			{
				_w.unlock();
			}
		}
	}
	
	@Override
	public void remove(T obj)
	{
		if (obj != null)
		{
			_w.lock();
			try
			{
				_objectMap.remove(obj.getObjectId());
			}
			finally
			{
				_w.unlock();
			}
		}
	}
	
	@Override
	public T get(int id)
	{
		_r.lock();
		try
		{
			return _objectMap.get(id);
		}
		finally
		{
			_r.unlock();
		}
	}
	
	@Override
	public boolean contains(T obj)
	{
		if (obj == null)
		{
			return false;
		}
		_r.lock();
		try
		{
			return _objectMap.containsValue(obj);
		}
		finally
		{
			_r.unlock();
		}
	}
	
	@Override
	public Iterator<T> iterator()
	{
		_r.lock();
		try
		{
			return _objectMap.values().iterator();
		}
		finally
		{
			_r.unlock();
		}
	}
}