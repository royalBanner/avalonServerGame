package net.sf.l2j.gameserver;

import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;
import net.sf.l2j.gameserver.lib.SqlUtils;
import net.sf.l2j.gameserver.model.L2Territory;

/**
 * Territory.
 * @author Balancer
 * @version 0.1.1, 2005-06-07
 */
public class Territory
{
	private static Logger _log = Logger.getLogger(TradeController.class.getName());
	private static final Territory _instance = new Territory();
	private static final Map<Integer, L2Territory> _territory = new FastMap<>();
	
	public static Territory getInstance()
	{
		return _instance;
	}
	
	private Territory()
	{
		// load all data at server start
		reload_data();
	}
	
	public int[] getRandomPoint(int terr)
	{
		return _territory.get(terr).getRandomPoint();
	}
	
	public int getProcMax(int terr)
	{
		return _territory.get(terr).getProcMax();
	}
	
	public void reload_data()
	{
		_territory.clear();
		Integer[][] point = SqlUtils.get2DIntArray(new String[]
		{
			"loc_id",
			"loc_x",
			"loc_y",
			"loc_zmin",
			"loc_zmax",
			"proc"
		}, "locations", "loc_id > 0");
		for (Integer[] row : point)
		{
			// _log.info("row = "+row[0]);
			Integer terr = row[0];
			if (terr == null)
			{
				_log.warning("Null territory!");
				continue;
			}
			
			if (_territory.get(terr) == null)
			{
				L2Territory t = new L2Territory(terr);
				_territory.put(terr, t);
			}
			_territory.get(terr).add(row[1], row[2], row[3], row[4], row[5]);
		}
	}
}