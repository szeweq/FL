package szewek.fl.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;

public final class FLNetUtilServer implements FLNetUtil {
	public static final FLNetUtilServer THIS = new FLNetUtilServer();

	private FLNetUtilServer() {}

	@Override
	public Tuple<IThreadListener, EntityPlayer> preprocess(FMLProxyPacket p, Side s) {
		final NetHandlerPlayServer h = (NetHandlerPlayServer) p.handler();
		if (h != null) {
			final EntityPlayer ep = h.player;
			final IThreadListener itl = ep.getServer();
			if (itl != null)
				return new Tuple<>(itl, ep);
		}
		return null;
	}

	@Override
	public void decode(FLNetMsg m, EntityPlayer p, Side s) {
		m.srvmsg(p);
	}

	@Override
	public Side check(INetHandler h) {
		return h instanceof NetHandlerPlayServer ? Side.SERVER : null;
	}
}
