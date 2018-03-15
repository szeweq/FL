package szewek.fl.network

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.INetHandler
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.util.IThreadListener
import net.minecraft.util.Tuple
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket
import net.minecraftforge.fml.relauncher.Side

class FLNetUtilServer private constructor() : FLNetUtil {

    override fun preprocess(p: FMLProxyPacket, s: Side): Tuple<IThreadListener, EntityPlayer>? {
        val h = p.handler() as NetHandlerPlayServer?
        if (h != null) {
            val ep = h.player
            val itl = ep.server
            if (itl != null)
                return Tuple(itl, ep)
        }
        return null
    }

    override fun decode(msg: FLNetMsg, p: EntityPlayer, s: Side) = msg.srvmsg(p)

    override fun check(h: INetHandler) = if (h is NetHandlerPlayServer) Side.SERVER else null

    companion object {
        val THIS = FLNetUtilServer()
    }
}
