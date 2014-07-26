package interfaces;

import net.kaleidos.dcchat.Message;

public interface NotificationListener {
	public void broadcastMessageReceived(Message message);
	public void directMessageReceived(Message message);
	public void error(String error);
	public void userConnected(String userSid, String userNick);
	public void userDisconnected(String userSid);
	public void serverDisconnect();
}
