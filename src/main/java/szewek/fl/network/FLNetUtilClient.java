package szewek.fl.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class FLNetUtilClient implements FLNetUtil {
	public static final FLNetUtilClient THIS = new FLNetUtilClient();

	private FLNetUtilClient() {}

	@Override
	public Tuple<IThreadListener, EntityPlayer> preprocess(final FMLProxyPacket p, final Side s) {
		if (s == Side.CLIENT) {
			final Minecraft mc = Minecraft.getMinecraft();
			return new Tuple<>(mc, mc.player);
		}
		return FLNetUtilServer.THIS.preprocess(p, s);
	}

	@Override
	public void decode(final FLNetMsg m, EntityPlayer p, final Side s) {
		if (p == null)
			p = Minecraft.getMinecraft().player;
		if (s == Side.CLIENT)
			m.climsg(p);
		else
			m.srvmsg(p);
	}

	@Override
	public Side check(final INetHandler h) {
		return h instanceof NetHandlerPlayClient ? Side.CLIENT : h instanceof NetHandlerPlayServer ? Side.SERVER : null;
	}
}
