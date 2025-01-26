package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 * @author Dezmond_snz Format: cddd
 */
public final class DlgAnswer extends L2GameClientPacket
{
	private static final String _C__C5_DLGANSWER = "[C] C5 DlgAnswer";
	private static Logger _log = Logger.getLogger(DlgAnswer.class.getName());
	
	private int _messageId;
	private int _answer, _unk;
	
	@Override
	protected void readImpl()
	{
		_messageId = readD();
		_answer = readD();
		_unk = readD();
	}
	
	@Override
	public void runImpl()
	{
		if (Config.DEBUG)
		{
			_log.fine(getType() + ": Answer acepted. Message ID " + _messageId + ", asnwer " + _answer + ", unknown field " + _unk);
		}
		if (_messageId == SystemMessageId.RESSURECTION_REQUEST.getId())
		{
			getClient().getActiveChar().reviveAnswer(_answer);
		}
		else if ((_messageId == 614) && Config.L2JMOD_ALLOW_WEDDING)
		{
			getClient().getActiveChar().EngageAnswer(_answer);
		}
		
	}
	
	@Override
	public String getType()
	{
		return _C__C5_DLGANSWER;
	}
}