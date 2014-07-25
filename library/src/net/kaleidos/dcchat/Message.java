package net.kaleidos.dcchat;

public class Message {
	private String text;
	private String userSid;
	
	// if userSid is null it means the message comes directly from the hub
	public Message(String userSid, String text) {
		super();
		this.text = text;
		this.userSid = userSid;
	}

	public String getText() {
		return text;
	}

	public String getUserSid() {
		return userSid;
	}
	 
}
