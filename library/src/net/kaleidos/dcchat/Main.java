package net.kaleidos.dcchat;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import interfaces.NotificationListener;

public class Main {
	TestingThread th = new TestingThread();

	private class TestingThread extends Thread {
		public NotificationListener nl = new NotificationListenerImp();
		public DCChat dcchat;

		public void run() {
			
			try {
				//this.dcchat = new DCChat("superalex", "dc.p2plibre.es", 2780, true, nl);
				this.dcchat = new DCChat("betatester", "dc.ekparty.org", 2783, false, nl);
				System.out.println("INIT" +  this.dcchat);				
				// this.dcchat = new DCChat("superalex", "dc.ekparty.org", 2783, false, nl);
				this.dcchat.connect();
			} catch (NoSuchAlgorithmException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void sendBroadcastMessage(String text, boolean isMe) throws IOException {
			this.dcchat.sendBroadcastMessage(text, isMe);
		}
		
		public void sendDirectMessage(String userSid, String text) throws IOException {
			this.dcchat.sendDirectMessage(userSid, text);
		}

	}

	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.th.start();
		Thread.sleep(3000);
		//m.th.sendBroadcastMessage("LIKES CATS", true);
		m.th.sendDirectMessage("ACR6", "hola Al");
//		m.th.sendDirectMessage(m.th.dcchat.getSsid(), "test");		
	}
}
