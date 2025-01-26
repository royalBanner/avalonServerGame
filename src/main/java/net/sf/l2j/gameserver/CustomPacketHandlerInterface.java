package net.sf.l2j.gameserver;

import java.nio.ByteBuffer;

import net.sf.l2j.gameserver.clientpackets.L2GameClientPacket;
import net.sf.l2j.gameserver.network.L2GameClient;

/**
 * This interface can be implemented by custom extensions to l2j to get packets before the normal processing of PacketHandler
 * @version $Revision: $ $Date: $
 * @author galun
 */
public interface CustomPacketHandlerInterface
{
	
	/**
	 * interface for a custom packethandler to ckeck received packets PacketHandler will take care of the packet if this function returns null.
	 * @param data the packet
	 * @param client the ClientThread
	 * @return a ClientBasePacket if the packet has been processed, null otherwise
	 */
	public L2GameClientPacket handlePacket(ByteBuffer data, L2GameClient client);
}