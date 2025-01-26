package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;

import com.l2jserver.mmocore.ReceivablePacket;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.serverpackets.L2GameServerPacket;

/**
 * Packets received by the game server from clients
 * @author KenM
 */
public abstract class L2GameClientPacket extends ReceivablePacket<L2GameClient>
{
	private static final Logger _log = Logger.getLogger(L2GameClientPacket.class.getName());
	
	@Override
	protected boolean read()
	{
		// System.out.println(this.getType());
		try
		{
			readImpl();
			return true;
		}
		catch (Throwable t)
		{
			_log.severe("Client: " + getClient().toString() + " - Failed reading: " + getType() + ";");
			t.printStackTrace();
		}
		return false;
	}
	
	protected abstract void readImpl();
	
	@Override
	public void run()
	{
		try
		{
			// flood protection
			if ((GameTimeController.getGameTicks() - getClient().packetsSentStartTick) > 10)
			{
				getClient().packetsSentStartTick = GameTimeController.getGameTicks();
				getClient().packetsSentInSec = 0;
			}
			else
			{
				getClient().packetsSentInSec++;
				if (getClient().packetsSentInSec > 12)
				{
					if (getClient().packetsSentInSec < 100)
					{
						sendPacket(new ActionFailed());
					}
					return;
				}
			}
			
			runImpl();
			if ((this instanceof MoveBackwardToLocation) || (this instanceof AttackRequest) || (this instanceof RequestMagicSkillUse))
			// could include pickup and talk too, but less is better
			{
				// Removes onspawn protection - player has faster computer than
				// average
				if (getClient().getActiveChar() != null)
				{
					getClient().getActiveChar().onActionRequest();
				}
			}
		}
		catch (Throwable t)
		{
			_log.severe("Client: " + getClient().toString() + " - Failed running: " + getType() + ";");
			t.printStackTrace();
		}
	}
	
	protected abstract void runImpl();
	
	protected final void sendPacket(L2GameServerPacket gsp)
	{
		getClient().sendPacket(gsp);
	}
	
	/**
	 * @return A String with this packet name for debuging purposes
	 */
	public abstract String getType();
}