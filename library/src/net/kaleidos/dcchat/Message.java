package net.kaleidos.dcchat;

public class Message {
	private String text;
	private String userSid;
	private String userNick;

	public Message(String text, String userSid, String userNick) {
		super();
		this.text = text;
		this.userSid = userSid;
		this.userNick = userNick;
	}

	public String getText() {
		return text;
	}

	public String getUserSid() {
		return userSid;
	}

	public String getUserNick() {
		return userNick;
	}
	 
	 
}
