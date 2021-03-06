package com.dhb.nettystudy.s07;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {

	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(2);

		ServerBootstrap b = new ServerBootstrap();
		try {
			ChannelFuture f = b.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ServerChildHandler());
						}
					})
					.bind(8888)
					.sync();
			System.out.println("server started!");
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}
}

class ServerChildHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Server.clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel ["+ctx.name()+"] 连接关闭，被移除。");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = null;
		if(msg instanceof ByteBuf) {
			try {
				buf = (ByteBuf) msg;
				System.out.println("==============================");
				byte[] bytes = new byte[buf.readableBytes()];
				buf.getBytes(buf.readerIndex(), bytes);
				System.out.println("server receive:"+new String(bytes));
				Server.clients.writeAndFlush(msg);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}
	}

}