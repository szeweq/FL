package szewek.fl.network;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import szewek.fl.FL;

import java.util.EnumMap;
import java.util.List;

@ChannelHandler.Sharable
public final class FLNetChannel extends SimpleChannelInboundHandler<FMLProxyPacket> {
	private final String chname;
	private final FMLEmbeddedChannel srvChan, cliChan;
	private final List<Class<? extends FLNetMsg>> ids;

	public FLNetChannel(final String name, final List<Class<? extends FLNetMsg>> list) {
		chname = name;
		ids = list;
		EnumMap<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel(name, this);
		srvChan = channels.get(Side.SERVER);
		cliChan = channels.get(Side.CLIENT);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
		try {
			final Side s = FL.PROXY.getNetUtil().check(msg.handler());
			Tuple<IThreadListener, EntityPlayer> tup = FL.PROXY.getNetUtil().preprocess(msg, s);
			if (tup == null) {
				FL.L.warn("No Tuple");
				return;
			}
			final IThreadListener itl = tup.getFirst();
			final EntityPlayer ep = tup.getSecond();
			PacketBuffer pb = (PacketBuffer) msg.payload();
			if (pb == null)
				pb = new PacketBuffer(msg.payload());
			final byte id = pb.readByte();
			final FLNetMsg fm = ids.get(id).newInstance();
			if (!itl.isCallingFromMinecraftThread()) {
				try {
					fm.decode(pb);
				} catch (Exception fx) {
					fm.exception(fx);
				}
				itl.addScheduledTask(new FLNetMsg.Decode(fm, ep, s));
			}
		} catch (Exception x) {
			FL.L.error("[FLNetChannel] Exception thrown while decoding!", x);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// fireUserEvent
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		FL.L.error("FLNetChannel exception", cause);
		super.exceptionCaught(ctx, cause);
	}

	private void send(FMLEmbeddedChannel ch, FLNetMsg msg, FMLOutboundHandler.OutboundTarget ot, Object arg) {
		final PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
		final byte id = (byte) ids.indexOf(msg.getClass());
		pb.writeByte(id);
		try {
			msg.encode(pb);
		} catch (Exception x) {
			msg.exception(x);
			return;
		}
		ch.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(ot);
		if (arg != null)
			ch.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(arg);
		ch.writeAndFlush(new FMLProxyPacket(pb, chname)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	public void to(FLNetMsg msg, EntityPlayerMP mp) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.PLAYER, mp);
	}

	public void toAll(FLNetMsg msg) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.ALL, null);
	}

	public void toAllAround(FLNetMsg msg, NetworkRegistry.TargetPoint tp) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT, tp);
	}

	public void toDimension(FLNetMsg msg, int dimId) {
		send(srvChan, msg, FMLOutboundHandler.OutboundTarget.DIMENSION, dimId);
	}

	public void toServer(FLNetMsg msg) {
		send(cliChan, msg, FMLOutboundHandler.OutboundTarget.TOSERVER, null);
	}
}
