package ch.xx.udptx.client.impl;

import java.net.SocketException;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

public class ClientImplTest extends TestCase {

	static ClientImpl client;
	
	public static void init() {
		try {
			client = new ClientImpl();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		client.addHost("localhost", 17899);
	}
	
	public static void main(String[] args) {

		init() ;
		try {
			while(true) {
				client.broadcast("now time "+new Date().toString());
				Thread.sleep(50*new Random().nextInt(100));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
