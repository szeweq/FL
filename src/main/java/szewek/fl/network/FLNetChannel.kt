package szewek.fl.network

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel
import net.minecraftforge.fml.common.network.FMLOutboundHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket
import net.minecraftforge.fml.relauncher.Side
import szewek.fl.FL

@ChannelHandler.Sharable
class FLNetChannel(private val chname: String, private val ids: List<Class<out FLNetMsg>>) : SimpleChannelInboundHandler<FMLProxyPacket>() {
	private val srvChan: FMLEmbeddedChannel
	private val cliChan: FMLEmbeddedChannel

	init {
		val channels = NetworkRegistry.INSTANCE.newChannel(chname, this)
		srvChan = channels[Side.SERVER]!!
		cliChan = channels[Side.CLIENT]!!
	}

	@Throws(Exception::class)
	override fun channelRead0(ctx: ChannelHandlerContext, msg: FMLProxyPacket) {
		try {
			val s = FL.PROXY.netUtil.check(msg.handler())
			val tup = FL.PROXY.netUtil.preprocess(msg, s!!)
			if (tup == null) {
				FL.L!!.warn("No Tuple")
				return
			}
			val itl = tup.first
			val ep = tup.second
			var pb: PacketBuffer? = msg.payload() as PacketBuffer
			if (pb == null)
				pb = PacketBuffer(msg.payload())
			val id = pb.readByte()
			val fm = ids[id.toInt()].newInstance()
			if (!itl.isCallingFromMinecraftThread) {
				try {
					fm.decode(pb)
				} catch (fx: Exception) {
					fm.exception(fx)
				}

				itl.addScheduledTask(FLNetMsg.Decode(fm, ep, s))
			}
		} catch (x: Exception) {
			FL.L!!.error("[FLNetChannel] Exception thrown while decoding!", x)
		}

	}

	@Throws(Exception::class)
	override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
		// fireUserEvent
	}

	@Throws(Exception::class)
	override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
		FL.L!!.error("FLNetChannel exception", cause)
		super.exceptionCaught(ctx, cause)
	}

	private fun send(ch: FMLEmbeddedChannel, msg: FLNetMsg, ot: FMLOutboundHandler.OutboundTarget, arg: Any?) {
		val pb = PacketBuffer(Unpooled.buffer())
		val id = ids.indexOf(msg.javaClass).toByte()
		pb.writeByte(id.toInt())
		try {
			msg.encode(pb)
		} catch (x: Exception) {
			msg.exception(x)
			return
		}

		ch.attr<FMLOutboundHandler.OutboundTarget>(FMLOutboundHandler.FML_MESSAGETARGET).set(ot)
		if (arg != null)
			ch.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(arg)
		ch.writeAndFlush(FMLProxyPacket(pb, chname)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
	}

	fun to(msg: FLNetMsg, mp: EntityPlayerMP) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.PLAYER, mp)
	}

	fun toAll(msg: FLNetMsg) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.ALL, null)
	}

	fun toAllAround(msg: FLNetMsg, tp: NetworkRegistry.TargetPoint) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT, tp)
	}

	fun toDimension(msg: FLNetMsg, dimId: Int) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.DIMENSION, dimId)
	}

	fun toServer(msg: FLNetMsg) {
		send(cliChan, msg, FMLOutboundHandler.OutboundTarget.TOSERVER, null)
	}
}
