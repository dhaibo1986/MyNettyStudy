package com.dhb.nettystudy.s06;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.awt.*;

public class ServerFrame extends Frame {

	public static final ServerFrame INSTANCE = new ServerFrame();

	Button btnStart = new Button("start");
	TextArea taLeft = new TextArea();
	TextArea taRight = new TextArea();
	Server server = new Server();

	public ServerFrame() {
		EventLoopGroup group = new NioEventLoopGroup(1);


	}

}
