package szewek.fl.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Internal utility functions that depend on side (CLIENT or SERVER)
 */
public interface FLNetUtil {
	Tuple<IThreadListener, EntityPlayer> preprocess(final FMLProxyPacket p, final Side s);
	void decode(final FLNetMsg msg, EntityPlayer p, final Side s);
	Side check(final INetHandler h);
}
