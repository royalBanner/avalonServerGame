package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ActionFailed;

/**
 * @version $Revision: 1.7.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class AttackRequest extends L2GameClientPacket
{
	// cddddc
	private int _objectId;
	@SuppressWarnings("unused")
	private int _originX;
	@SuppressWarnings("unused")
	private int _originY;
	@SuppressWarnings("unused")
	private int _originZ;
	@SuppressWarnings("unused")
	private int _attackId;
	
	private static final String _C__0A_ATTACKREQUEST = "[C] 0A AttackRequest";
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		_attackId = readC(); // 0 for simple click 1 for shift-click
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		// avoid using expensive operations if not needed
		L2Object target;
		if (activeChar.getTargetId() == _objectId)
		{
			target = activeChar.getTarget();
		}
		else
		{
			target = L2World.getInstance().findObject(_objectId);
		}
		if (target == null)
		{
			return;
		}
		if (activeChar.getTarget() != target)
		{
			target.onAction(activeChar);
		}
		else
		{
			if ((target.getObjectId() != activeChar.getObjectId()) && (activeChar.getPrivateStoreType() == 0) && (activeChar.getActiveRequester() == null))
			{
				// _log.config("Starting ForcedAttack");
				target.onForcedAttack(activeChar);
				// _log.config("Ending ForcedAttack");
			}
			else
			{
				sendPacket(new ActionFailed());
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__0A_ATTACKREQUEST;
	}
}