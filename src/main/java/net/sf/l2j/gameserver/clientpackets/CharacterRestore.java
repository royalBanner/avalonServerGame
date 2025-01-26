package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;

/**
 * @version $Revision: 1.4.2.1.2.2 $ $Date: 2005/03/27 15:29:29 $
 */
public final class CharacterRestore extends L2GameClientPacket
{
	private static final String _C__62_CHARACTERRESTORE = "[C] 62 CharacterRestore";
	// private static Logger _log = Logger.getLogger(CharacterRestore.class.getName());
	
	// cd
	private int _charSlot;
	
	@Override
	protected void readImpl()
	{
		_charSlot = readD();
	}
	
	@Override
	protected void runImpl()
	{
		try
		{
			getClient().markRestoredChar(_charSlot);
		}
		catch (Exception e)
		{
			
		}
		CharSelectInfo cl = new CharSelectInfo(getClient().getAccountName(), getClient().getSessionId().playOkID1, 0);
		sendPacket(cl);
		getClient().setCharSelection(cl.getCharInfo());
	}
	
	@Override
	public String getType()
	{
		return _C__62_CHARACTERRESTORE;
	}
}