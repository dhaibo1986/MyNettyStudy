package com.dhb.nettystudy.s06;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame  extends Frame {

	public static final ClientFrame INSTANCE = new ClientFrame();

	TextArea ta = new TextArea();
	TextField tf = new TextField();
	Client client = null;

	public ClientFrame() {
		this.setSize(600,400);
		this.setLocation(100,20);
		this.setVisible(true);
		this.add(ta,BorderLayout.CENTER);
		this.add(tf,BorderLayout.SOUTH);
		this.setVisible(true);
		tf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.send(tf.getText());
				ta.setText(ta.getText()+"\n"+tf.getText());
				tf.setText("");

			}
		});
		client = new Client();
		client.connect();
	}

	public void updateText(String text) {
		ta.setText(ta.getText()+"\n"+text);
	}

	public static void main(String[] args) {
		new ClientFrame();
	}

}
