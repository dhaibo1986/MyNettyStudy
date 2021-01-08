package com.dhb.nettystudy.s02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

		public void connect() {
			EventLoopGroup group = new NioEventLoopGroup(1);
			Bootstrap b = new Bootstrap();

			try {
				ChannelFuture f = b.group(group).channel(NioSocketChannel.class)
						.connect("localhost",8888);
				f.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if(!future.isSuccess()) {
							System.out.println("not connect!");
						}else {
							System.out.println("connected!");
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

	public static void main(String[] args) {
		Client c = new Client();
		c.connect();
	}

}
