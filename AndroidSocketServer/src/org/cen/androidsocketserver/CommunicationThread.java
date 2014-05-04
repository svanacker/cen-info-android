package org.cen.androidsocketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommunicationThread implements Runnable {

		private Socket clientSocket;

		private BufferedReader input;
		
		private InputStream inputStream;
		
		private ThreadCallback threadCallBack;
		
		public CommunicationThread(Socket clientSocket, ThreadCallback threadCallBack) {
			this.clientSocket = clientSocket;
			this.threadCallBack = threadCallBack;
			try {
				inputStream = clientSocket.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				this.input = new BufferedReader(inputStreamReader);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (true) {
				try {
					if (inputStream.available() <= 0) {
						continue;
					}
					String read = input.readLine();
					if (read != null && !read.isEmpty() ) {
						threadCallBack.onGetContent(read, 2);
					}
					// updateConversationHandler.post(new updateUIThread(read));

				} catch (IOException e) {
					threadCallBack.onGetContent("ioException", 2);
				}
			}
			// threadCallBack.onGetContent("end of communication Thread", 2);
		}
	}
