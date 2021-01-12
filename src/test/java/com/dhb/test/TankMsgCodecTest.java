package com.dhb.test;

import com.dhb.nettystudy.s13.TankMsg;
import com.dhb.nettystudy.s13.TankMsgDecoder;
import com.dhb.nettystudy.s13.TankMsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TankMsgCodecTest {

	@Test
	public void testTankMsgEncoder() {
		TankMsg msg = new TankMsg(10,10);
		EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder());
		ch.writeOutbound(msg);
		ByteBuf buf = (ByteBuf) ch.readOutbound();
		int x = buf.readInt();
		int y = buf.readInt();
		Assertions.assertTrue(x==10 && y==10);
		buf.release();
	}

	@Test
	public void testTankMsgEncoder2() {
		ByteBuf buf = Unpooled.buffer();
		TankMsg msg = new TankMsg(10,10);
		buf.writeInt(msg.getX());
		buf.writeInt(msg.getY());

		EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder(),new TankMsgDecoder());
		ch.writeInbound(buf.duplicate());
		TankMsg tm = ch.readInbound();
		Assertions.assertTrue(tm.getX()==10&&tm.getY()==10);
	}
}
