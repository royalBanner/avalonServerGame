package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class ChangeMoveType2 extends L2GameClientPacket
{
	private static final String _C__1C_CHANGEMOVETYPE2 = "[C] 1C ChangeMoveType2";
	
	private boolean _typeRun;
	
	@Override
	protected void readImpl()
	{
		_typeRun = readD() == 1;
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (_typeRun)
		{
			player.setRunning();
		}
		else
		{
			player.setWalking();
		}
	}
	
	@Override
	public String getType()
	{
		return _C__1C_CHANGEMOVETYPE2;
	}
}