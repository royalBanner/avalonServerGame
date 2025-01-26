package net.sf.l2j.util;

import javolution.util.FastMap;

/**
 * @author Julian
 * @param <K>
 * @param <V>
 */
public class L2FastMap<K extends Object, V extends Object> extends FastMap<K, V>
{
	static final long serialVersionUID = 1L;
	
	public interface I2ForEach<K, V>
	{
		public boolean forEach(K key, V obj);
		
		public Entry<K, V> getNext(Entry<K, V> priv);
	}
	
	public final boolean ForEach(I2ForEach<K, V> func, boolean sync)
	{
		if (sync)
		{
			synchronized (this)
			{
				return forEachP(func);
			}
		}
		return forEachP(func);
	}
	
	private boolean forEachP(I2ForEach<K, V> func)
	{
		for (Entry<K, V> e = head(), end = tail(); (e = func.getNext(e)) != end;)
		{
			if (!func.forEach(e.getKey(), e.getValue()))
			{
				return false;
			}
		}
		return true;
	}
}