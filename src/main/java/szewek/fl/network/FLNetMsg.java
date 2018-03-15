package szewek.fl.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import szewek.fl.FL;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public abstract class FLNetMsg {
	protected boolean broken = true;

	/**
	 * Decodes a message from a buffer
	 *
	 * @param pb Buffer
	 * @throws IOException Usually comes from methods used with [PacketBuffer]
	 */
	public abstract void decode(PacketBuffer pb) throws IOException;

	/**
	 * Encodes a message into a buffer
	 *
	 * @param pb Buffer
	 * @throws IOException Usually comes from methods used with [PacketBuffer]
	 */
	public abstract void encode(PacketBuffer pb) throws IOException;

	/**
	 * Handles an exception
	 *
	 * @param x Exception thrown while using this message
	 */
	public void exception(Exception x) {
	}

	/**
	 * Reads message data for use in server
	 *
	 * @param p Player
	 */
	public void srvmsg(EntityPlayer p) {
	}

	/**
	 * Reads message data for use in client
	 *
	 * @param p Player
	 */
	@SideOnly(Side.CLIENT)
	public void climsg(EntityPlayer p) {
	}

	public static final class Decode implements Runnable {
		private final FLNetMsg msg;
		private final EntityPlayer player;
		private final Side side;


		public Decode(FLNetMsg msg, EntityPlayer p, Side side) {
			this.msg = msg;
			player = p;
			this.side = side;
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
