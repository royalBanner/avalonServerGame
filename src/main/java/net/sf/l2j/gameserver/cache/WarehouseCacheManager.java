package net.sf.l2j.gameserver.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author -Nemesiss-
 */
public class WarehouseCacheManager
{
	private static WarehouseCacheManager _instance;
	protected final Map<L2PcInstance, Long> _cachedWh;
	protected final long _cacheTime;
	
	public static WarehouseCacheManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new WarehouseCacheManager();
		}
		return _instance;
	}
	
	private WarehouseCacheManager()
	{
		_cacheTime = Config.WAREHOUSE_CACHE_TIME * 60000L; // 60*1000 = 60000
		_cachedWh = new ConcurrentHashMap<L2PcInstance, Long>();
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new CacheScheduler(), 120000, 60000);
	}
	
	public void addCacheTask(L2PcInstance pc)
	{
		_cachedWh.put(pc, System.currentTimeMillis());
	}
	
	public void remCacheTask(L2PcInstance pc)
	{
		_cachedWh.remove(pc);
	}
	
	public class CacheScheduler implements Runnable
	{
		@Override
		public void run()
		{
			long cTime = System.currentTimeMillis();
			for (L2PcInstance pc : _cachedWh.keySet())
			{
				if ((cTime - _cachedWh.get(pc)) > _cacheTime)
				{
					pc.clearWarehouse();
					_cachedWh.remove(pc);
				}
			}
		}
	}
}