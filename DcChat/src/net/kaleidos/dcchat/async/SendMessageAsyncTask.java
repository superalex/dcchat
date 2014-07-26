package net.kaleidos.dcchat.async;

import net.kaleidos.dcchat.DCChat;

public class SendMessageAsyncTask extends Thread{
	DCChat dcchat;
	String msg;
	String sid;
	
	

    public SendMessageAsyncTask(DCChat dcchat, String msg, String sid) {
		super();
		this.dcchat = dcchat;
		this.msg = msg;
		this.sid = sid;
		
	}



	@Override
	public void run() {
		try {
    		if (sid == null) {
    			dcchat.sendBroadcastMessage(msg, false);
    		} else{
    			dcchat.sendDirectMessage(sid, msg);
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
}
