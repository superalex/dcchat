package net.kaleidos.dcchat.listener;

import android.util.Log;
import net.kaleidos.dcchat.Message;
import interfaces.NotificationListener;

public class DccNotificationListener implements NotificationListener {

	@Override
	public void broadcastMessageReceived(Message message) {
		// TODO Auto-generated method stub
		Log.d("DccNotificationListener", message.getText());

	}

	@Override
	public void directMessageReceived(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userConnected(String userSid, String userNick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void userDisconnected(String userSid) {
		// TODO Auto-generated method stub

	}

}
