package szewek.fl.network

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.INetHandler
import net.minecraft.util.IThreadListener
import net.minecraft.util.Tuple
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket
import net.minecraftforge.fml.relauncher.Side

/**
 * Internal utility functions that depend on side (CLIENT or SERVER)
 */
interface FLNetUtil {
    fun preprocess(p: FMLProxyPacket, s: Side): Tuple<IThreadListener, EntityPlayer>?
    fun decode(msg: FLNetMsg, p: EntityPlayer, s: Side)
    fun check(h: INetHandler): Side?
}
