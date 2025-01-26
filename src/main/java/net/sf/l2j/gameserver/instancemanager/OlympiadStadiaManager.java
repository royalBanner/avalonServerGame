/*
 * Copyright © 2004-2020 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.instancemanager;

import java.util.logging.Logger;

import javolution.util.FastList;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.zone.type.L2OlympiadStadiumZone;

public class OlympiadStadiaManager
{
	protected static Logger _log = Logger.getLogger(OlympiadStadiaManager.class.getName());
	
	private static OlympiadStadiaManager _instance;
	
	public static final OlympiadStadiaManager getInstance()
	{
		if (_instance == null)
		{
			System.out.println("Initializing OlympiadStadiaManager");
			_instance = new OlympiadStadiaManager();
		}
		return _instance;
	}
	
	private FastList<L2OlympiadStadiumZone> _olympiadStadias;
	
	public OlympiadStadiaManager()
	{
	}
	
	public void addStadium(L2OlympiadStadiumZone arena)
	{
		if (_olympiadStadias == null)
		{
			_olympiadStadias = new FastList<>();
		}
		_olympiadStadias.add(arena);
	}
	
	public final L2OlympiadStadiumZone getStadium(L2Character character)
	{
		for (L2OlympiadStadiumZone temp : _olympiadStadias)
		{
			if (temp.isCharacterInZone(character))
			{
				return temp;
			}
		}
		return null;
	}
	
	@Deprecated
	public final L2OlympiadStadiumZone getOlympiadStadiumById(int olympiadStadiumId)
	{
		for (L2OlympiadStadiumZone temp : _olympiadStadias)
		{
			if (temp.getStadiumId() == olympiadStadiumId)
			{
				return temp;
			}
		}
		return null;
	}
}
