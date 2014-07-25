package net.kaleidos.dcchat;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import interfaces.NotificationListener;

public class Main {
	TestingThread th = new TestingThread();

	private class TestingThread extends Thread {
		public NotificationListener nl = new NotificationListenerImp();
		public DCChat dcchat;

		public void run() {
			
			try {
				byte[] unencodedPid = new byte[24];
				new Random().nextBytes(unencodedPid);
				
				this.dcchat = new DCChat("superalex3", "dc.p2plibre.es", 2780, true, unencodedPid, nl);
				//this.dcchat = new DCChat("betatester2", "dc.ekparty.org", 2783, false, unencodedPid, nl);
				System.out.println("INIT" +  this.dcchat);				
				// this.dcchat = new DCChat("superalex", "dc.ekparty.org", 2783, false, unencodedPid, nl);
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
		//m.th.sendDirectMessage("XN65", "hola Al");
//		m.th.sendDirectMessage(m.th.dcchat.getSsid(), "test");		
	}
}
