package net.kaleidos.dcchat.async;

import java.io.IOException;
import java.util.Random;

import net.kaleidos.dcchat.Base32;
import net.kaleidos.dcchat.DCChat;
import net.kaleidos.dcchat.listener.DccNotificationListener;
import net.kaleidos.dcchat.listener.Messageable;
import android.os.AsyncTask;
import android.util.Log;

public class DcChatAsyncTask extends AsyncTask<String, Void, String> {
	
	private static final String ADCS="adcs";
	



	DCChat dcchat;
	DccNotificationListener listener;
	Messageable messageable;
	String pid;
	String host;
	int port = 2783;
	String protocol="adcs";
	String userName;

    public DcChatAsyncTask(Messageable messageable, String pid, String server, String userName) {
		super();
		this.messageable = messageable;
		this.pid = pid;
		this.userName = userName;
		
		String[] s0 = server.split("://");
		String s1;
		
		if (s0.length == 2){
			protocol = s0[0];
			s1 = s0[1];
		} else {
			s1 = s0[0];
		}
		
		String[] s = s1.split(":");
		
		
		this.host = s[0];
		
		if (s.length == 2){
			this.port = Integer.parseInt(s[1]);
		}
	}
    
    public DCChat getDcchat() {
		return dcchat;
	}

	@Override
    protected String doInBackground(String... params) {
    	try {
    		listener = new DccNotificationListener();
    		listener.addMessageable(messageable);
    		
    		    		
			this.dcchat = new DCChat(userName, host, port, ADCS.equals(protocol), Base32.decode(pid), listener);
			this.dcchat.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
       
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
    
    public void disconnect() {
    	try {
    		Log.d ("DcChatAsyncTask", "disconnect");
			dcchat.disconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
       


}
