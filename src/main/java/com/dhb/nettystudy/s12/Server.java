package com.dhb.nettystudy.s12;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {

	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public static void main(String[] args) {


	}

	public void serverStop() {
		for(Channel c :clients) {
			if(c.isActive()) {
				c.flush();
				c.close();
			}
		}
		if(null != workerGroup) {
			workerGroup.shutdownGracefully();
		}
		if(null != bossGroup) {
			bossGroup.shutdownGracefully();
		}
	}

	public void serverStart() {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(2);
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
			ServerFrame.INSTANCE.updateServerMsg("server started!");
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
		ServerFrame.INSTANCE.updateServerMsg("channel ["+ctx.name()+"] 连接关闭，被移除。");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		Server.clients.remove(ctx.channel());
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
				String s = new String(bytes);
				ServerFrame.INSTANCE.updateClientMsg(s);
				if("_bye_".equals(s)){
					System.out.println("客户端"+ctx.name()+"要求退出!");
					ServerFrame.INSTANCE.updateServerMsg("客户端"+ctx.name()+"要求退出!");
					Server.clients.remove(ctx.channel());
					ctx.close();
				}else {
					System.out.println("server receive:"+new String(bytes));
					ServerFrame.INSTANCE.updateServerMsg("server receive:"+new String(bytes));
					Server.clients.writeAndFlush(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}
	}

}