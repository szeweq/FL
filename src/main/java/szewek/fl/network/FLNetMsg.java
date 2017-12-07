package szewek.fl.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import szewek.fl.FL;

import java.io.IOException;

/**
 * Basic message class used in {@link FLNetChannel}
 */
public abstract class FLNetMsg {
	protected boolean broken = true;

	/**
	 * Decodes a message from a buffer
	 * @param pb Buffer
	 * @throws IOException Usually comes from methods used with {@link PacketBuffer}
	 */
	protected abstract void decode(PacketBuffer pb) throws IOException;

	/**
	 * Encodes a message into a buffer
	 * @param pb Buffer
	 * @throws IOException Usually comes from methods used with {@link PacketBuffer}
	 */
	protected abstract void encode(PacketBuffer pb) throws IOException;

	/**
	 * Handles an exception
	 * @param x Exception thrown while using this message
	 */
	protected void exception(Exception x) {
	}

	/**
	 * Reads message data for use in server
	 * @param p Player
	 */
	protected void srvmsg(EntityPlayer p) {
	}

	/**
	 * Reads message data for use in client
	 * @param p Player
	 */
	@SideOnly(Side.CLIENT)
	protected void climsg(EntityPlayer p) {
	}

	static final class Decode implements Runnable {
		private final FLNetMsg msg;
		private final EntityPlayer player;
		private final Side side;

		Decode(FLNetMsg m, EntityPlayer p, Side s) {
			msg = m;
			player = p;
			side = s;
		}

		@Override
		public void run() {
			try {
				FL.PROXY.getNetUtil().decode(msg, player, side);
			} catch (Exception x) {
				msg.exception(x);
			}
		}
	}
}
