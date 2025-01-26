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

import javolution.util.FastList;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.zone.type.L2ArenaZone;

public class ArenaManager
{
	private static ArenaManager _instance;
	
	public static final ArenaManager getInstance()
	{
		if (_instance == null)
		{
			System.out.println("Initializing ArenaManager");
			_instance = new ArenaManager();
		}
		return _instance;
	}
	
	private FastList<L2ArenaZone> _arenas;
	
	public ArenaManager()
	{
	}
	
	public void addArena(L2ArenaZone arena)
	{
		if (_arenas == null)
		{
			_arenas = new FastList<>();
		}
		
		_arenas.add(arena);
	}
	
	public final L2ArenaZone getArena(L2Character character)
	{
		for (L2ArenaZone temp : _arenas)
		{
			if (temp.isCharacterInZone(character))
			{
				return temp;
			}
		}
		return null;
	}
}
