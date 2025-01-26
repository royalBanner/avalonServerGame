package net.sf.l2j.gameserver.clientpackets;

/**
 * @author zabbix Lets drink to code!
 */
public final class DummyPacket extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		
	}
	
	@Override
	public void runImpl()
	{
		
	}
	
	@Override
	public String getType()
	{
		return "DummyPacket";
	}
}