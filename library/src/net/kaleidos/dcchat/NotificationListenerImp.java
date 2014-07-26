package net.kaleidos.dcchat;
import java.util.HashMap;

import interfaces.NotificationListener;

public class NotificationListenerImp implements NotificationListener {
	private HashMap<String, String> nicksBySid = new HashMap<String, String>();

	@Override
	public void broadcastMessageReceived(Message message) {
		String userNick = nicksBySid.get(message.getUserSid());
		System.out.println("-> " + userNick + ": " + message.getText());
	}

	@Override
	public void directMessageReceived(Message message) {
		String userNick = nicksBySid.get(message.getUserSid());
		System.out.println("Privado " + userNick + ": " + message.getText());
	}

	@Override
	public void error(String error) {
		System.out.println("ERROR: " + error);
	}

	@Override
	public void userConnected(String userSid, String userNick) {
		if (userNick.equals("superalex")){
			System.out.println("SUPER: " + userSid);
		}
		nicksBySid.put(userSid, userNick);
	}

	@Override
	public void userDisconnected(String userSid) {
		System.out.println("Disconnected: " + userSid);
	}

	@Override
	public void serverDisconnect() {
		// TODO Auto-generated method stub
		
	}

}
