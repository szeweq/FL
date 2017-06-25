package szewek.fl.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;


/**
 * Internal utility functions that depend on side (CLIENT or SERVER)
 */
enum FLNetUtil {
	CLI {
		@Override
		Tuple<IThreadListener, EntityPlayer> preprocess(final FMLProxyPacket p, final Side s) {
			if (s == Side.CLIENT) {
				final Minecraft mc = Minecraft.getMinecraft();
				return new Tuple<>(mc, mc.player);
			}
			return SRV.preprocess(p, s);
		}

		@Override
		void decode(final FLNetMsg m, EntityPlayer p, final Side s) {
			if (p == null)
				p = Minecraft.getMinecraft().player;
			if (s == Side.CLIENT)
				m.climsg(p);
			else
				m.srvmsg(p);
		}

		@Override
		Side check(final INetHandler h) {
			return h instanceof NetHandlerPlayClient ? Side.CLIENT : h instanceof NetHandlerPlayServer ? Side.SERVER : null;
		}
	}, SRV {
		@Override
		Tuple<IThreadListener, EntityPlayer> preprocess(final FMLProxyPacket p, final Side s) {
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
		void decode(final FLNetMsg m, EntityPlayer p, final Side s) {
			m.srvmsg(p);
		}

		@Override
		Side check(final INetHandler h) {
			return h instanceof NetHandlerPlayServer ? Side.SERVER : null;
		}
	};

	abstract Tuple<IThreadListener, EntityPlayer> preprocess(final FMLProxyPacket p, final Side s);
	abstract void decode(final FLNetMsg msg, EntityPlayer p, final Side s);
	abstract Side check(final INetHandler h);

	static final FLNetUtil FN = FMLCommonHandler.instance().getSide() == Side.CLIENT ? CLI : SRV;


}
