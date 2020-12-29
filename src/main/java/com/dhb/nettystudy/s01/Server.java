package com.dhb.nettystudy.s01;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket();
		ss.bind(new InetSocketAddress(8888));
		Socket s = ss.accept();
		System.out.println("a client connect !");
	}
}
