package net.kaleidos.dcchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ServersActivity extends ActionBarActivity {
	public static final String PREFS_NAME = "DcChatPreferences";
	private static final String DEFAULT_SERVER_LIST = "adcs://dc.p2plibre.es:2780;adcs://dc.ekparty.org;";
	private ArrayList<String> serverList;
	private ArrayAdapter<String> serverListAdapter;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servers);
		
		
		
		
		this.setTitle((String) getResources().getText(R.string.select_server));
		
		serverList = new ArrayList<String>();  
		
		final ListView listview = (ListView) findViewById(R.id.serverList);
	    
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    String serversString = settings.getString("servers", "");
		
		if (serversString.equals("")){
			serversString = DEFAULT_SERVER_LIST;
		}
			
		serverList.addAll( Arrays.asList(serversString.split(";")) );
		
	    
		serverList.add((String) getResources().getText(R.string.new_server));
	    
	    
	    

		
		
		serverListAdapter = new ArrayAdapter<String>(this, R.layout.server_row, serverList);
	    listview.setAdapter( serverListAdapter ); 
	    
	    listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				
				selectServer(position);
			}
		});
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parentView, View childView, int position, long id) {
				// TODO Auto-generated method stub
				removeServerDialog(position);
				return true;
			}
		});
	    
	    
	}
	
	public void removeServerDialog(final int position){
		if (position != serverList.size() -1) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Delete server");
			alert.setMessage("Are you sure?");
			

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {			  
				removeServer(position);
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
			
			alert.show();
		}
	}
	
	public void removeServer(int position){
		if (position != serverList.size() -1) {
			serverList.remove(position);
			serverListAdapter.notifyDataSetChanged();
		}
	
	}
	
	public void selectServer(int position){
	
		
		if (position == serverList.size() -1) {
			//Create new server
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Create new server");
			alert.setMessage("Server address");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  String value = input.getText().toString();
			  createServer(value);
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

			alert.show();
			
		} else {
			//Connect to server
		}
		
		
	}
	
	private void createServer(String name){
		serverList.remove(serverList.size() - 1);
		serverList.add(name);		
		serverList.add((String) getResources().getText(R.string.new_server));
		serverListAdapter.notifyDataSetChanged();
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.servers, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
