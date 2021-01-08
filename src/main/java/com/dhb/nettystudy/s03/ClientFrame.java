package com.dhb.nettystudy.s03;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame  extends Frame {

	TextArea ta = new TextArea();
	TextField tf = new TextField();


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
				ta.setText(ta.getText()+"\n"+tf.getText());
				tf.setText("");
			}
		});
		new Client().connect();
	}

	public static void main(String[] args) {
		new ClientFrame();
	}

}
