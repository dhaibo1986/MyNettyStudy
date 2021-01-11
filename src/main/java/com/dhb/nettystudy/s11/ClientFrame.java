package com.dhb.nettystudy.s11;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame  extends Frame {

	public static final ClientFrame INSTANCE = new ClientFrame();

	public static final String LINE_SEP = System.getProperty("line.separator","\n");

	TextArea ta = new TextArea();
	TextField tf = new TextField();
	Client client = null;

	public ClientFrame() {
		this.setSize(600,400);
		this.setLocation(100,20);
		this.setVisible(true);
		this.add(ta,BorderLayout.CENTER);
		this.add(tf,BorderLayout.SOUTH);
		tf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.send(tf.getText());
//				ta.setText(ta.getText()+"\r\n"+tf.getText());
				tf.setText("");

			}
		});

		//增加关闭按钮事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ClientFrame.exit();
			}
		});
	}

	public void updateText(String text) {
		ta.setText(ta.getText()+LINE_SEP+text);
	}


	private void connectToServer() {
		client = new Client();
		client.connect();
	}

	public static void start() {
		ClientFrame frame = ClientFrame.INSTANCE;
		frame.setVisible(true);
		frame.connectToServer();
	}

	public static void exit() {
		ClientFrame frame = ClientFrame.INSTANCE;
		frame.client.closeConnect();
		System.exit(0);
	}

	public static void main(String[] args) {
		ClientFrame.start();
	}

}
