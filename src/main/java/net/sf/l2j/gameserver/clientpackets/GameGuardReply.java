package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author zabbix Lets drink to code! Unknown Packet:ca 0000: 45 00 01 00 1e 37 a2 f5 00 00 00 00 00 00 00 00 E....7..........
 */
public class GameGuardReply extends L2GameClientPacket
{
	private static final String _C__CA_GAMEGUARDREPLY = "[C] CA GameGuardReply";
	
	@Override
	protected void readImpl()
	{
		
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		getClient().setGameGuardOk(true);
	}
	
	@Override
	public String getType()
	{
		return _C__CA_GAMEGUARDREPLY;
	}
	
}