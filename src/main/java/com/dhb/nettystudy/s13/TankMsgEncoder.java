package com.dhb.nettystudy.s13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class  TankMsgEncoder extends MessageToByteEncoder<TankMsg> {

	@Override
	protected void encode(ChannelHandlerContext ctx, TankMsg msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getX());
		out.writeInt(msg.getY());
	}
}
