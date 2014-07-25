package net.kaleidos.dcchat.listener;

import net.kaleidos.dcchat.Message;

public interface Messageable {
	public void receiveBroadcastMessage(Message message);
	public void receiveDirectMessage(Message message);
	public void userConnected(String userSid, String userNick);
	public void userDisconnected(String userSid);
	public void receiveError(String error);
}
