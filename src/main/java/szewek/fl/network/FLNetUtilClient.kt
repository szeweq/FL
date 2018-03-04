package szewek.fl.network

import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.INetHandler
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.util.IThreadListener
import net.minecraft.util.Tuple
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
class FLNetUtilClient private constructor() : FLNetUtil {

    override fun preprocess(p: FMLProxyPacket, s: Side): Tuple<IThreadListener, EntityPlayer>? {
        if (s == Side.CLIENT) {
            val mc = Minecraft.getMinecraft()
            return Tuple(mc, mc.player)
        }
        return FLNetUtilServer.THIS.preprocess(p, s)
    }

    override fun decode(msg: FLNetMsg, p: EntityPlayer, s: Side) {
        if (s == Side.CLIENT)
            msg.climsg(p)
        else
            msg.srvmsg(p)
    }

    override fun check(h: INetHandler): Side? {
        return if (h is NetHandlerPlayClient) Side.CLIENT else if (h is NetHandlerPlayServer) Side.SERVER else null
    }

    companion object {
        val THIS = FLNetUtilClient()
    }
}
