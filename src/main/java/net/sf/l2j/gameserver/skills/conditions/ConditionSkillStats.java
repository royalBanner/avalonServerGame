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
package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * @author mkizub
 */
public class ConditionSkillStats extends Condition
{
	private final Stats _stat;
	
	public ConditionSkillStats(Stats stat)
	{
		super();
		_stat = stat;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		if (env.skill == null)
		{
			return false;
		}
		return env.skill.getStat() == _stat;
	}
}
