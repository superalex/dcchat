import gnu.crypto.hash.Tiger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class DCChat {
	private String ssid;
	private String pid;
	private String cid;
	private boolean connected = true;
	private Socket socket;
	private DataOutputStream os;
	private BufferedReader is;
	private HashMap<String, String> nicksBySid = new HashMap<String, String>();
	
	public void sendDirectMessage(String userSid, String text) {
		//TODO
	}
	
	public void sendBroadcastMessage(String text) {
		//TODO
	}
	
	public void disconnect() throws IOException {
		this.connected = false;
		os.close();
		is.close();
		socket.close();			
		System.out.println("FIN");		
	}
	
	public DCChat(String username, String host, int port, boolean useSSL) throws UnknownHostException, IOException, NoSuchAlgorithmException{
		//Preparing the sockets
		SocketFactory factory;
		if (useSSL){
			NaiveTrustProvider.setAlwaysTrust(true);
			 factory = SSLSocketFactory.getDefault();			
		}
		else {
			factory =  SocketFactory.getDefault();
		}
		socket = factory.createSocket(host, port);
		os = new DataOutputStream(socket.getOutputStream());
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		//Initialize connection - let's say the hub we support BASE and TIGR hashing
		os.writeBytes("HSUP ADBASE ADTIGR\n");
		
		while (connected) {
       	 String message = is.readLine();
       	 if (message != null && message.length() >= 4){
       		 String context = message .substring(0, 1);       		 
       		 String action = message.substring(1, 4);
       		 String text;
       		 String userSid;
       		 String userNick;
       		 
       		 //System.out.println(message);
       		 if (action.equals("SID") && context.equals("I")){
       			 //Information SID
   		 		this.ssid = message.substring(5);       		 		
   		 		
   		 		//PID and CID Generation
   		 		//TODO: randomize
   				byte[] unencodedPid = new byte[] {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4};
   				this.pid = Base32.encode(unencodedPid);       				
   				Tiger tt = new Tiger();
   				tt.update(unencodedPid, 0, unencodedPid.length);
   				byte[] digest = tt.digest();
   				this.cid = Base32.encode(digest);
			
				//TODO: extra params 
   		 		os.writeBytes("BINF "+ this.ssid +" SS6666666666666666666666666 CT8 SL10 NI"+username+" ID"+cid+" PD"+pid+"\n");       		 		   		 		
   				//os.writeBytes("BINF "+ this.ssid +" ID"+cid+" PD"+pid+"\n");
   		 		
       		 } else if (action.equals("MSG") && context.equals("B")) {       			 
       		 	//Broadcast MeSsaGe
		 		text = message.substring(5);
		 		userSid = text.substring(0,4);
		 		userNick = this.nicksBySid.get(userSid);
		 		System.out.println("-> "+userNick + ": " + text.substring(5).replaceAll("\\\\s", " "));
		 		
       		} else if (action.equals("INF") && context.equals("I")) {
       			//Information
       			text = message.substring(5);
   		 		System.out.println("IINF"+text.replaceAll("\\\\s", " "));
   		 		
       		} else if (action.equals("INF") && context.equals("B")) {
       			//Broadcast INFormation
   		 		if (message.contains(" NI")){
   		 			userSid = message.substring(5, 9);
   		 			int niPos = message.indexOf(" NI");
   		 			userNick = message.substring(niPos+3);
   		 			userNick = userNick.split("\\ ")[0];
   		 			nicksBySid.put(userSid, userNick);
   		 		}
   		 		
       		} else if (action.equals("STA") && context.equals("I")) {
       			//Info Status code
       			//Error messages
       			//TODO: raise ERROR
       			text = message.split(" ")[2].replaceAll("\\\\s", " ");
   		 		System.out.println("ERROR: "+text);
       		}
	       	//TODO: Direct message
       	 }
       }	
	}
}
