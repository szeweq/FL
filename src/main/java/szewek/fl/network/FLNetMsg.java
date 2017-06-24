package szewek.fl.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public abstract class FLNetMsg {
	protected boolean broken = true;

	abstract void decode(PacketBuffer pb) throws IOException;

	abstract void encode(PacketBuffer pb) throws IOException;

	abstract void exception(Exception x);

	public void srvmsg(EntityPlayer p) {
	}

	@SideOnly(Side.CLIENT)
	public void climsg(EntityPlayer p) {
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
				FLNetUtil.FN.decode(msg, player, side);
			} catch (Exception x) {
				msg.exception(x);
			}
		}
	}
}
