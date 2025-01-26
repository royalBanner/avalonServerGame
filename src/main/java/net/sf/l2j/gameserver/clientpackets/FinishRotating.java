package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.serverpackets.StopRotation;

/**
 * @version $Revision: 1.1.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class FinishRotating extends L2GameClientPacket
{
	private static final String _C__4B_FINISHROTATING = "[C] 4B FinishRotating";
	
	private int _degree;
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	protected void readImpl()
	{
		_degree = readD();
		_unknown = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (getClient().getActiveChar() == null)
		{
			return;
		}
		StopRotation sr = new StopRotation(getClient().getActiveChar(), _degree);
		getClient().getActiveChar().broadcastPacket(sr);
	}
	
	@Override
	public String getType()
	{
		return _C__4B_FINISHROTATING;
	}
}