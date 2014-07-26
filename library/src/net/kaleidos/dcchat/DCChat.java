package net.kaleidos.dcchat;
import gnu.crypto.hash.Tiger;
import interfaces.NotificationListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;


public class DCChat {
	private String ssid;
	public String getSsid() {
		return ssid;
	}

	private String pid;
	private String cid;
	private String username;
	private String host;
	private int port;
	private boolean useSSL;
	private boolean connected = true;
	private Socket socket;
	private DataOutputStream os;
	private BufferedReader is;
	private byte[] unencodedPid;
	private NotificationListener notificationListener;

	public DCChat(String username, String host, int port, boolean useSSL,
			byte[] unencodedPid,
			NotificationListener notificationListener)
			throws UnknownHostException, IOException, NoSuchAlgorithmException {
		this.username = username;
		this.notificationListener = notificationListener;
		this.useSSL = useSSL;
		this.host = host;
		this.port = port;
		this.unencodedPid = unencodedPid;
		//A valid way of generating it
		/*byte[] unencodedPid = new byte[24];
		new Random().nextBytes(unencodedPid);*/		
		
	}
		
	public void connect() throws UnknownHostException, IOException{
		initializeCommunication(this.useSSL, this.host, this.port);

		while (connected) {
			String message = is.readLine();
			if (message != null && message.length() > 4) {
				String context = message.substring(0, 1);
				String action = message.substring(1, 4);
				String text = message.substring(5);

				//System.out.println("------>" + message);
				if (action.equals("SID") && context.equals("I")) {
					receiveSID(text);

				} else if (action.equals("MSG") && context.equals("B")) {
					// Broadcast MeSsaGe
					receiveBroadcastMessage(text);

				} else if (action.equals("INF") && context.equals("I")) {
					// Information
					receiveInformationMessage(text);

				} else if (action.equals("INF") && context.equals("B")) {
					// Broadcast INFormation (info about user nick related to a
					// sid)
					if (message.contains(" NI")) {
						receiveUserNick(text);
					}
				} else if (action.equals("MSG") && (context.equals("E") || context.equals("D")) ) {
					// Direct message
					receiveDirectMessage(text);					
				} else if (action.equals("STA") && context.equals("I")) {
					// Info Status code - Error messages
					receiveError(text);
				} else if (action.contains("QUI")){
					receiveQuit(text);
				}
				
			}
			
		}
		os.close();
		is.close();
		socket.close();	
		notificationListener.serverDisconnect();
	}

	public void sendDirectMessage(String userSid, String text) throws IOException {
		String message = String.format("EMSG %s %s %s PM%s\n", 
			this.ssid, userSid, prepareText(text), this.ssid);
		os.write(message.getBytes("UTF-8"));
	}

	public void sendBroadcastMessage(String text, boolean isMe) throws IOException {				
		String message = String.format("BMSG %s %s", this.ssid,  prepareText(text));
		if (isMe) {
			message += " ME1"; 
		}
		message += "\n";
		os.write(message.getBytes("UTF-8"));
	}

	public void disconnect() throws IOException {
		this.connected = false;
	}

	private String cleanText(String text) {
		text = text.replaceAll("\\\\s", " ");
		text = text.replaceAll("\\\\n", "\n");
		text = text.replace("\\\\", "\\");
		return text;
	}
	
	private String prepareText(String text) {
		text = text.replace(" ", "\\s");
		text = text.replace("\n", "\\\\n");
		return text;
	}


	private void initializeCommunication(boolean useSSL, String host, int port)
			throws UnknownHostException, IOException {
		// Preparing the sockets
		SocketFactory factory;
		NaiveTrustProvider.setAlwaysTrust(true);
		if (useSSL) {			
			factory = SSLSocketFactory.getDefault();
		} else {
			factory = SocketFactory.getDefault();
		}
		socket = factory.createSocket(host, port);
		os = new DataOutputStream(socket.getOutputStream());
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Initialize connection - let's say the hub we support BASE and TIGR
		// hashing
		os.writeBytes("HSUP ADBASE ADTIGR\n");
	}

	private void receiveSID(String input) throws IOException {
		// Information SID
		this.ssid = input;

		// PID and CID Generation		
		this.pid = Base32.encode(unencodedPid);
		Tiger tt = new Tiger();
		tt.update(unencodedPid, 0, unencodedPid.length);
		byte[] digest = tt.digest();
		this.cid = Base32.encode(digest);

		// TODO: extra params
		String extraParams = "SS9999999 SL10";
		String binfCommand = String.format("BINF %s %s NI%s ID%s PD%s\n", 
			this.ssid, extraParams, this.username, this.cid, this.pid);
		
		os.writeBytes(binfCommand);
	}

	private void receiveBroadcastMessage(String input) {
		String userSid = input.substring(0, 4);
		String text = input.substring(5);
		Message broadcastMessage = new Message(userSid, cleanText(text));
		notificationListener.broadcastMessageReceived(broadcastMessage);
	}

	private void receiveInformationMessage(String input) {
		Message broadcastMessage = new Message(null, cleanText(input));
		notificationListener.broadcastMessageReceived(broadcastMessage);
	}

	private void receiveUserNick(String input) {
		String userSid = input.substring(0, 4);
		int niPos = input.indexOf(" NI");
		String userNick = input.substring(niPos + 3);
		userNick = userNick.split("\\ ")[0];
		notificationListener.userConnected(userSid, userNick);
	}

	private void receiveDirectMessage(String input) {
		String userSid = input.substring(0, 4);
		String text = input.split("\\ ")[2];
		Message directMessage = new Message(userSid, cleanText(text));
		notificationListener.directMessageReceived(directMessage);
	}
	
	private void receiveError(String input) {
		// Info Status code - Error messages
		String text = this.cleanText(input.split(" ")[1]);
		notificationListener.error(text);
	}
	
	private void receiveQuit(String input) {
		String userSid = input.substring(0, 4);
		notificationListener.userDisconnected(userSid);
	}
}
