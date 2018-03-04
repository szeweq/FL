package szewek.fl.network

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import szewek.fl.FL

import java.io.IOException

/**
 * Basic message class used in [FLNetChannel]
 */
abstract class FLNetMsg {
    protected var broken = true

    /**
     * Decodes a message from a buffer
     * @param pb Buffer
     * @throws IOException Usually comes from methods used with [PacketBuffer]
     */
    @Throws(IOException::class)
    internal abstract fun decode(pb: PacketBuffer)

    /**
     * Encodes a message into a buffer
     * @param pb Buffer
     * @throws IOException Usually comes from methods used with [PacketBuffer]
     */
    @Throws(IOException::class)
    internal abstract fun encode(pb: PacketBuffer)

    /**
     * Handles an exception
     * @param x Exception thrown while using this message
     */
    internal fun exception(x: Exception) {}

    /**
     * Reads message data for use in server
     * @param p Player
     */
    fun srvmsg(p: EntityPlayer) {}

    /**
     * Reads message data for use in client
     * @param p Player
     */
    @SideOnly(Side.CLIENT)
    fun climsg(p: EntityPlayer) {
    }

    internal class Decode(private val msg: FLNetMsg, private val player: EntityPlayer, private val side: Side) : Runnable {

        override fun run() {
            try {
                FL.PROXY!!.netUtil.decode(msg, player, side)
            } catch (x: Exception) {
                msg.exception(x)
            }

        }
    }
}
