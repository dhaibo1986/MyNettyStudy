package com.dhb.nettystudy.s13;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {

	public static final String LINE_SEP = System.getProperty("line.separator","\n");

	public static final ServerFrame INSTANCE = new ServerFrame();

	Button btnStart = new Button("start");
	TextArea taLeft = new TextArea();
	TextArea taRight = new TextArea();
	Server server = new Server();

	public ServerFrame() {
		this.setSize(1600,600);
		this.setLocation(300,30);
		this.add(btnStart,BorderLayout.NORTH);
		Panel p = new Panel(new GridLayout(1,2));
		p.add(taLeft);
		p.add(taRight);
		this.add(p);

		//增加关闭按钮事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("ServerFrame 点击关闭按钮退出。。");
				ServerFrame.exit();
			}
		});

		this.btnStart.addActionListener((e) -> {
			server.serverStart();
		});



	}

	public static void main(String[] args) {
		ServerFrame.start();
		ServerFrame.INSTANCE.server.serverStart();
	}

	public static void start() {
		ServerFrame frame = ServerFrame.INSTANCE;
		frame.setVisible(true);
	}

	public static void exit() {
		ServerFrame frame = ServerFrame.INSTANCE;
		frame.server.serverStop();
		System.exit(0);
	}

	public void updateServerMsg(String msg) {
		this.taLeft.setText(taLeft.getText()+LINE_SEP+msg);
	}

	public void updateClientMsg(String msg) {
		this.taRight.setText(taLeft.getText()+LINE_SEP+msg);
	}
}
