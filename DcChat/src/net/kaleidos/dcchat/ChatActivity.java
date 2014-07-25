package net.kaleidos.dcchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import net.kaleidos.dcchat.async.DcChatAsyncTask;
import net.kaleidos.dcchat.listener.Messageable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChatActivity extends ActionBarActivity implements Messageable {
	private static final String PUBLIC_CHAT = "Public Chat";
	
	DcChatAsyncTask dcChatAsyncTask;	
	ConcurrentHashMap<String, ArrayList<String>> privateMessages = new ConcurrentHashMap<String, ArrayList<String>>();
	
	private ArrayList<String> chatNames = new ArrayList<String>();
	private ArrayList<String> userNames = new ArrayList<String>();
	ConcurrentHashMap<String, String> users = new ConcurrentHashMap<String, String>();
	ConcurrentHashMap<String, String> usersSid = new ConcurrentHashMap<String, String>();
	
	String currentChat = PUBLIC_CHAT;
	TextView textList;
	ListView chatList;
	ListView userList;
	ScrollView textScroll;
	TextView textView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		setTitle(PUBLIC_CHAT);
		
		//Data.dcChatAsyncTask.addMessageable(this);
		
		textView = (TextView) findViewById(R.id.textList);
		
		
		dcChatAsyncTask = new DcChatAsyncTask(this);
		//Data.dcChatAsyncTask = dcChatAsyncTask;
		dcChatAsyncTask.execute("");
		
		
		chatList = (ListView) findViewById(R.id.chatList);
		chatList.setVisibility(View.INVISIBLE);
		
		chatList.setAdapter(new ArrayAdapter<String>(this, R.layout.chat_row, chatNames));
		
		
		
		chatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				
				selectChat(position);
			}
		});
		
		
		userList = (ListView) findViewById(R.id.userList);
		userList.setVisibility(View.INVISIBLE);
		
		userList.setAdapter(new ArrayAdapter<String>(this, R.layout.user_row, userNames));
		
		
		
		userList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				
				selectUser(position);
			}
		});
		
		
		
		textScroll = (ScrollView) findViewById(R.id.textScroll);
		textList = (TextView) findViewById(R.id.textList);
		textList.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {			
				textScroll.fullScroll(ScrollView.FOCUS_DOWN);
		    }
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.list_chats) {			
			ChatActivity.this.listChats();
			return true;
		}
		if (id == R.id.list_users) {
			ChatActivity.this.listUsers();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void listChats(){
		currentChat = null;		
		ChatActivity.this.setTitle("Chat list");
		textScroll.setVisibility(View.INVISIBLE);
		userList.setVisibility(View.INVISIBLE);
		
		
		chatNames.clear();
		chatNames.add(PUBLIC_CHAT);	
		for (String s:privateMessages.keySet()){
			if (!PUBLIC_CHAT.equals(s)){
				chatNames.add(getNick(s));
			}
		}
		
		chatList.setVisibility(View.VISIBLE);
		
		
		((ArrayAdapter<String>)chatList.getAdapter()).notifyDataSetChanged();
		
		
	}
	
	private void selectChat(int position){
		if (position > 0) {
			String chatName = chatNames.get(position);
			selectChat(users.get(chatName));
		} else {
			selectChat(PUBLIC_CHAT);
		}
	}
	
	private void selectChat(String sid){
		if (!PUBLIC_CHAT.equals(sid)){
			ChatActivity.this.setTitle(getNick(sid));
		} else {
			ChatActivity.this.setTitle(PUBLIC_CHAT);
		}
		
		chatList.setVisibility(View.INVISIBLE);
		userList.setVisibility(View.INVISIBLE);
		
		textView.setText("");
				
		for (String s: privateMessages.get(sid)){
			textView.append(s+"\n");
		}
		
		textScroll.setVisibility(View.VISIBLE);
		
		
		currentChat = sid;
		
		
	}
	
	
	private void listUsers(){
		currentChat = null;
		ChatActivity.this.setTitle("All users");
		textScroll.setVisibility(View.INVISIBLE);
		chatList.setVisibility(View.INVISIBLE);
		
		userNames.clear();
		for (String s:users.keySet()){			
			userNames.add(s);
		}
		
		
		userList.setVisibility(View.VISIBLE);
		
		((ArrayAdapter<String>)userList.getAdapter()).notifyDataSetChanged();
		
	}
	
	private void selectUser(int position){
		String userName = userNames.get(position);
		String sId = users.get(userName);
		if (!privateMessages.containsKey(sId)){
			privateMessages.put(sId, new ArrayList<String>());
		}
		selectChat(sId);
	}
	
	

	@Override
	public void receiveBroadcastMessage(final Message message) {
		
		
		
		String nick;
		if (message.getUserSid() == null){
			nick = "***SERVER***";
		} else {
			nick = getNick(message.getUserSid());
		}
		
		final String text = "<"+nick+"> "+message.getText();
		
		if (!privateMessages.containsKey(PUBLIC_CHAT)){
			privateMessages.put(PUBLIC_CHAT, new ArrayList<String>());
		}
		
		
		privateMessages.get(PUBLIC_CHAT).add(text);
		
		if (currentChat == PUBLIC_CHAT) {
			runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	textView.append(text+"\n");
	            }
	        });
		}
		
	}
	
	@Override
	public void receiveDirectMessage(final Message message) {
		
		if (!privateMessages.containsKey(message.getUserSid())){
			privateMessages.put(message.getUserSid(), new ArrayList<String>());
		}
		
		final String text = "<"+getNick(message.getUserSid())+"> "+message.getText();
		
		privateMessages.get(message.getUserSid()).add(text);
		
		
		if (currentChat == message.getUserSid()) {
		
			runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	textView.append(text+"\n");
	            }
	        });
		}
		
	}

	@Override
	public void userConnected(String userSid, final String userNick) {
		
		users.put(userNick, userSid);
		
		usersSid.put(userSid, userNick);
	
	}

	@Override
	public void userDisconnected(String userSid) {
		
		
	}
	
	
	private String getNick(String sid){
		String nick = null;
		if (sid != null){
			if (usersSid.containsKey(sid)){
				nick = usersSid.get(sid);
			} else {
				nick = sid;
			}
		}
		return nick;
		
	}
}