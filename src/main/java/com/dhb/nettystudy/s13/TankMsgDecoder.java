package com.dhb.nettystudy.s13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankMsgDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//解决TCP拆包和粘包问题 TCP有序 等待数据字节长度达到所需长度之后再处理
		if(in.readableBytes()<8){
			return;
		}
		in.markReaderIndex();
		//x y的顺序取决于写入的时候encode的顺序
		int x = in.readInt();
		int y = in.readInt();
		out.add(new TankMsg(x,y));
	}
}
