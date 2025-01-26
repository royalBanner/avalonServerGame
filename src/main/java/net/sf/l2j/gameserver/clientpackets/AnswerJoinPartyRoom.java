package net.sf.l2j.gameserver.clientpackets;

/**
 * Format: (ch) d
 * @author -Wooden-
 */
public final class AnswerJoinPartyRoom extends L2GameClientPacket
{
	private static final String _C__D0_15_ANSWERJOINPARTYROOM = "[C] D0:15 AnswerJoinPartyRoom";
	@SuppressWarnings("unused")
	private int _requesterID; // not tested, just guessed
	
	@Override
	protected void readImpl()
	{
		_requesterID = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// TODO
		// System.out.println("C5:AnswerJoinPartyRoom: d: "+_requesterID);
	}
	
	@Override
	public String getType()
	{
		return _C__D0_15_ANSWERJOINPARTYROOM;
	}
}