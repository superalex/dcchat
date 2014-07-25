package net.kaleidos.dcchat.async;

import java.util.Random;

import net.kaleidos.dcchat.DCChat;
import net.kaleidos.dcchat.listener.DccNotificationListener;
import net.kaleidos.dcchat.listener.Messageable;
import android.os.AsyncTask;

public class DcChatAsyncTask extends AsyncTask<String, Void, String> {
	



	DCChat dcchat;
	DccNotificationListener listener;
	Messageable messageable;
	

    public DcChatAsyncTask(Messageable messageable) {
		super();
		this.messageable = messageable;
	}
    
    public DCChat getDcchat() {
		return dcchat;
	}

	@Override
    protected String doInBackground(String... params) {
    	try {
    		listener = new DccNotificationListener();
    		listener.addMessageable(messageable);
    		
    		
    		
    		byte[] unencodedPid = new byte[24];
			new Random().nextBytes(unencodedPid);
    		
    		
			this.dcchat = new DCChat("betatester2000", "dc.ekparty.org", 2783, false, unencodedPid, listener);
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
