package org.cen.androidsocketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class ServerThread implements Runnable {

	public static final int SERVERPORT = 6000;

	private ServerSocket serverSocket;

	private ThreadCallback threadCallBack;

	public ServerThread(ThreadCallback threadCallback) {
		this.threadCallBack = threadCallback;
	}

	public void run() {
		Map<String, InetAddress> localIpAddresses = NetworkUtils.getLocalIpAddress();
		Set<String> ips = localIpAddresses.keySet();
		for (String ip : ips) {
			threadCallBack.onGetContent(ip, 1);
		}
		InetAddress localIpAddress = localIpAddresses.get("192.168.43.1");
		Socket socket = null;
		try {
			// serverSocket = new ServerSocket(SERVERPORT, 50, localIpAddress);
			serverSocket = new ServerSocket(SERVERPORT);
		} catch (IOException e) {
			threadCallBack.onGetContent("error", 2);
			e.printStackTrace();
		}
		BufferedReader input = null;
		InputStream inputStream = null;
		while (true) {
			try {
				if (socket == null) {
					socket = serverSocket.accept();
				}
				inputStream = socket.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				input = new BufferedReader(inputStreamReader);
				
				if (inputStream == null || inputStream.available() <= 0) {
					continue;
				}
				
				String read = input.readLine();
				if (read != null && !read.isEmpty() ) {
					threadCallBack.onGetContent(read, 2);
				}
			} catch (IOException e) {
				threadCallBack.onGetContent("end of server", 2);
				e.printStackTrace();
			}
		}
		// threadCallBack.onGetContent("end of server", 2);
	}

	public void close() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
	}
}