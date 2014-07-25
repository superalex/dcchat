package net.kaleidos.dcchat.listener;

import java.util.ArrayList;

import android.util.Log;
import net.kaleidos.dcchat.Message;
import interfaces.NotificationListener;

public class DccNotificationListener implements NotificationListener {
	
	ArrayList<Messageable> messageables = new ArrayList<Messageable>();

	@Override
	public void broadcastMessageReceived(Message message) {
		// TODO Auto-generated method stub
		Log.d("DccNotificationListener", message.getText());
		
		for (Messageable messageable:messageables){
			messageable.receiveBroadcastMessage(message);
		}

	}

	@Override
	public void directMessageReceived(Message message) {
		// TODO Auto-generated method stub
		for (Messageable messageable:messageables){
			messageable.receiveDirectMessage(message);
		}

	}

	@Override
	public void error(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userConnected(String userSid, String userNick) {
		for (Messageable messageable:messageables){
			messageable.userConnected(userSid, userNick);
		}
	}

	@Override
	public void userDisconnected(String userSid) {
		for (Messageable messageable:messageables){
			messageable.userDisconnected(userSid);
		}

	}
	
	public void addMessageable(Messageable messageable){
		messageables.add(messageable);
    }

}
