package com.dhb.nettystudy.s11;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;


public class Client {

	private Channel channel = null;

	private Client c = null;

	private EventLoopGroup group;

	public static void main(String[] args) {
		ClientFrame.start();
	}

	public void connect() {
		group = new NioEventLoopGroup(1);
		Bootstrap b = new Bootstrap();

		try {
			ChannelFuture f = b.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									ByteBuf buf = null;
									try {
										buf = (ByteBuf) msg;
										byte[] bytes = new byte[buf.readableBytes()];
										buf.getBytes(buf.readerIndex(), bytes);
										String msgAccepted = new String(bytes);
										ClientFrame.INSTANCE.updateText(msgAccepted);

									} finally {
										if (buf != null) {
											ReferenceCountUtil.release(buf);
										}
									}
								}
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									//channel第一次连接上之后写出一个字符
									ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
									ctx.writeAndFlush(buf);
								}

								@Override
								public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
									//服务端关闭当前连接
									System.out.println("当前连接"+ctx.name()+"已被服务端关闭！客户端将退出。。");
									ClientFrame.exit();
								}
							});
						}
					})
					.connect("localhost", 8888);
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						System.out.println("not connect!");
					} else {
						System.out.println("connected!");
						channel = future.channel();
					}
				}
			});
			f.sync();
			System.out.println("...");
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public void closeConnect() {
		if(channel.isActive()) {
			System.out.println("客户端的当前连接处于可用状态，将先向服务端发送关闭连接的命令。");
			this.send("_bye_");
		}
		this.channel.close();
		this.group.shutdownGracefully();
	}


	public void send(String msg) {
		ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
		channel.writeAndFlush(buf);
	}



}

