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
	



	DCChat dcchat;
	DccNotificationListener listener;
	Messageable messageable;
	String pid;
	String host;
	int port = 2783;
	

    public DcChatAsyncTask(Messageable messageable, String pid, String server) {
		super();
		this.messageable = messageable;
		this.pid = pid;
		
		String[] s = server.split(":");
		
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
    		
    		
			this.dcchat = new DCChat("betatester2000", host, port, false, Base32.decode(pid), listener);
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
