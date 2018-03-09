package ch.xx.udptx.client.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import ch.xx.udptx.client.Client;

public class ClientImpl implements Client {

	private int localPort = 17899;
	
	private Map<String,Integer> targetHosts = new HashMap<>();
	
    private DatagramSocket datagramSocket;

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public void addHost(String host,int port) {
		targetHosts.put(host, port);
	}
	
	public ClientImpl() throws SocketException {
		datagramSocket = new DatagramSocket(localPort);
		datagramSocket.setReuseAddress(true);
	}

	private volatile boolean listened;
	
	private Object lock;
	
	public void startListening() {
		if(!listened) {
			listened = true;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					synchronized (lock) {
						while(listened) {
							byte[] receBuf = new byte[1024];
							DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
							try {
								datagramSocket.receive(recePacket);
								String receStr = new String(recePacket.getData(), 0 , recePacket.getLength());
					            System.out.println("Client Rece Ack:" + receStr);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	@Override
	public void broadcast(String msg) {
		targetHosts.forEach((ip,port)->{
			send(msg, ip, port);
		});
	}
	
	public void send(String msg,String targethost, int port) {
		try {
			byte[] buf = msg.getBytes("utf-8");
			InetAddress address = InetAddress.getByName(targethost);
			DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, address , port);
			datagramSocket.send(datagramPacket);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
