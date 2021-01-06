package com.dhb.nettystudy.http.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class HttpClient {

	public static void main(String[] args) throws Exception{
		HttpClient client = new HttpClient();
		client.connect("127.0.0.1",9000);
	}

	public void connect(String host, int port) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//客户端接收到的httpResponse响应，需要用HttpRespinsesDecoder进行解码
							ch.pipeline().addLast(new HttpResponseDecoder());
							//客户端发送的httprequest请求，需要适用HttpRequestEncoder进行编码
							ch.pipeline().addLast(new HttpRequestEncoder());
							ch.pipeline().addLast(new HttpClientInboundHandler());
						}
					});

			ChannelFuture f = b.connect(host,port).sync();
			URI uri = new URI("http://127.0.0.1:9000");
			String msg = "Are you OK ?";
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
					uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
			//构建http请求
			request.headers().set(HttpHeaderNames.HOST,host);
			request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
			request.headers().set(HttpHeaderNames.HOST,request.content().readableBytes());
			//发送http请求
			f.channel().write(request);
			f.channel().flush();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

}
