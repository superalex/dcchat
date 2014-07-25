package net.kaleidos.dcchat.async;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import net.kaleidos.dcchat.DCChat;
import net.kaleidos.dcchat.listener.DccNotificationListener;
import android.os.AsyncTask;

public class DcChatAsyncTask extends AsyncTask<String, Void, String> {
	DCChat dcchat;

    @Override
    protected String doInBackground(String... params) {
    	try {
    		DccNotificationListener listener = new DccNotificationListener();
			this.dcchat = new DCChat("betatester1", "dc.ekparty.org", 2783, false, listener);
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

}
