package net.kaleidos.dcchat;

import java.util.ArrayList;
import java.util.Arrays;

import net.kaleidos.dcchat.async.DcChatAsyncTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

public class ServersActivity extends ActionBarActivity {
	public static final String PREFS_NAME = "DcChatPreferences";
	private static final String DEFAULT_SERVER_LIST = "adcs://dc.p2plibre.es:2780;adcs://dc.ekparty.org;";
	private ArrayList<String> serverList;
	private ArrayAdapter<String> serverListAdapter;
	private SharedPreferences settings;

	String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servers);

		this.setTitle((String) getResources().getText(R.string.select_server));

		serverList = new ArrayList<String>();

		final ListView listview = (ListView) findViewById(R.id.serverList);

		settings = getSharedPreferences(PREFS_NAME, 0);

		userName = settings.getString("username",
				"user" + System.currentTimeMillis());

		String serversString = settings.getString("servers", "");

		if (serversString.equals("")) {
			serversString = DEFAULT_SERVER_LIST;
			serverList.addAll(Arrays.asList(serversString.split(";")));
			serverList
					.add((String) getResources().getText(R.string.new_server));
		} else {
			serverList.addAll(Arrays.asList(serversString.split(";")));
		}

		serverListAdapter = new ArrayAdapter<String>(this, R.layout.server_row,
				serverList);
		listview.setAdapter(serverListAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView,
					int position, long id) {

				selectServer(position);
			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parentView,
					View childView, int position, long id) {
				// TODO Auto-generated method stub
				removeServerDialog(position);
				return true;
			}
		});

	}

	public void removeServerDialog(final int position) {
		if (position != serverList.size() - 1) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Delete server");
			alert.setMessage("Are you sure?");

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							removeServer(position);
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();
		}
	}

	private void setUserName() {
		// Create new server
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Set your user name");
		alert.setMessage("User name");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setText(userName);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				userName = value;
				Editor editor = settings.edit();

				editor.putString("username", userName);
				editor.commit();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();

	}

	public void selectServer(int position) {

		if (position == serverList.size() - 1) {
			// Create new server
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Create new server");
			alert.setMessage("Server address");

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String value = input.getText().toString();
							createServer(value);
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();

		} else {
			// Connect to server
			connectToServer(position);
		}

	}

	private void connectToServer(int position) {

		if (position != serverList.size() - 1) {
			String server = serverList.get(position);
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("server", server);
			intent.putExtra("username", userName);

			ServersActivity.this.startActivity(intent);

		}

	}

	private void createServer(String name) {
		serverList.remove(serverList.size() - 1);
		serverList.add(name);
		serverList.add((String) getResources().getText(R.string.new_server));
		serverListAdapter.notifyDataSetChanged();

		saveServerList();

	}

	public void removeServer(int position) {
		if (position != serverList.size() - 1) {
			serverList.remove(position);
			serverListAdapter.notifyDataSetChanged();
			saveServerList();
		}
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
			setUserName();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveServerList() {
		StringBuffer sb = new StringBuffer();
		for (String s : serverList) {
			sb.append(s);
			sb.append(";");
		}

		if (sb.length() > 0) {
			// Remove last ";"
			sb.deleteCharAt(sb.length() - 1);
		}

		Editor editor = settings.edit();

		editor.putString("servers", sb.toString());
		editor.commit();

	}
}