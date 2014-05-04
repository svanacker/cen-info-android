package org.cen.androidsocketserver;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import or.cen.androidsocketserver.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements ThreadCallback, Runnable {

	private ServerThread serverThread = null;

	private Handler updateConversationHandler;

	private TextView text1;

	private ArrayBlockingQueue<String> queue1;
	private ArrayBlockingQueue<String> queue2;

	private TextView text2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		updateConversationHandler = new Handler();

		queue1 = new ArrayBlockingQueue<String>(100);
		queue2 = new ArrayBlockingQueue<String>(100);

		setContentView(R.layout.activity_main);

		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			}
		});

		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);

		serverThread = new ServerThread(this);
		new Thread(serverThread).start();

		// runOnUiThread(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			serverThread.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class updateUIThread implements Runnable {
		private String msg;
		private int textZoneIndex;

		public updateUIThread(String str, int textZoneIndex) {
			this.msg = str;
			this.textZoneIndex = textZoneIndex;
		}

		@Override
		public void run() {
			// String newText = text.getText().toString() + "Client Says: " +
			// msg
			// + "\n";

			String newText = "Client : " + msg + "\n";
			if (textZoneIndex == 1) {
				text1.setText(text1.getText().toString() + "\n" + newText);
			} else {
				text2.setText(newText);
			}
		}
	}

	public void run() {
		while (true) {
			if (!queue1.isEmpty()) {
				String element1 = queue1.poll();
				if (element1 != null) {
					String newText = "Client : " + element1 + "\n";
					text1.setText(text1.getText().toString() + "\n" + newText);
				}
			}
			if (!queue2.isEmpty()) {
				String element2 = queue1.poll();
				if (element2 != null) {
					String newText = "Client : " + element2 + "\n";
					text2.setText(text2.getText().toString() + "\n" + newText);
				}
			}
		}
	}

	@Override
	public void onGetContent(String s, int textZoneIndex) {
		// if (textZoneIndex == 1) {
		// queue1.add(s);
		// } else {
		// queue2.add(s);
		// }
		updateUIThread thread = new updateUIThread(s, textZoneIndex);
		// new Thread(thread).start();

		updateConversationHandler.post(thread);
		// thread.run();
	}
}