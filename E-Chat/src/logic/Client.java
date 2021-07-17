package logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	private String username;
	private int port;
	private Socket client;
	String serverIp;
	
	public Client () {
		port = 5678;
		serverIp = "localhost";
		// Let server know you are connecting
        try {
        	System.out.println("Connecting to " + serverIp + " on port " + port);
        	client = new Socket(serverIp, port);
        	System.out.println("Just connected to " + client.getRemoteSocketAddress());
        	
        	OutputStream outToServer = client.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	out.writeUTF("Hello from Usama " + client.getLocalSocketAddress());
        	
        	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	
        	System.out.println("Server says " + in.readUTF());
        }
		catch(IOException e) {
	 	   e.printStackTrace();
	 	}
	}
	
	public boolean AuthenticateLogin (String username, String password) {
		// Send login info to server to authenticate
		try {
			this.username = username;
        	OutputStream outToServer = client.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	out.writeUTF("Login " + username + " " + password);
        	
        	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	
        	if (in.readUTF().equals("True")) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
		catch(IOException e) {
	 	   e.printStackTrace();
	 	}
		return true;
	}
	public boolean SignUp(String username, String password, String email, String FirstName, String LastName) {
		try {
			//sending to server all the user data
			OutputStream outToServer = client.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	out.writeUTF("SignUp " + username + " " + password+ " " + email+ " " + FirstName+ " " + LastName);
        	
        	//recieving from server
        	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	
        	//if this username already exists
        	if(in.readUTF().equals("exists:username")) {
        		System.out.println("This username already exists");
        		return false;
        	}
        	else {
        		return true;
        	}
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
		return true;
	}
	public boolean Logout(String username) throws IOException {
		try {
			OutputStream outToServer = client.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	out.writeUTF("Logout " +username);
        	
        	//recieving from server
        	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	if(in.readUTF().equals("True")){
        		//client.close();
        	}
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
		return true;
	}
	
	//for ending connection after the app has been closed
	public void endConnection() {
		try {
			OutputStream outToServer = client.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	if(username==null)
        		out.writeUTF("exit");
        	else
        		out.writeUTF("exit "+username);
        	return;
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
	}
	
	//for adding friend
	public boolean searchFriend(String username) {
		try {
			OutputStream outToServer = client.getOutputStream();
        	DataOutputStream out = new DataOutputStream(outToServer);
        	out.writeUTF("Search_Friend "+username);
        	
        	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	
        	if(in.readUTF().equals("True")) {
        		return true;
        	}
        	
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
		return false;
	}
	
	//for adding friends 
	public boolean addFriend(String username, String From) {
		try {
			OutputStream outToServer = client.getOutputStream();
	    	DataOutputStream out = new DataOutputStream(outToServer);
	    	out.writeUTF("Add_Friend "+username +" "+From);
	    	
	    	
	    	
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
		return false;
	}
	
	public String[] getRequests(String username ) {
		try {
			//System.out.println("username: "+username);
			OutputStream outToServer = client.getOutputStream();
	    	DataOutputStream out = new DataOutputStream(outToServer);
	    	out.writeUTF("Get_Requests "+username); 	
	    	
	    	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	
        	String msg = in.readUTF();
        	String[] strArray = msg.split("\\s+");
        	return strArray;
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
		return null;
	}
	
	public void acceptRequest(String username, String acceptUser) {
		try{
			OutputStream outToServer = client.getOutputStream();
	    	DataOutputStream out = new DataOutputStream(outToServer);
	    	out.writeUTF("Accept_Requests "+username+" "+acceptUser);
	    	
	    	
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
	}
	public String[] getFriends(String username) {
		try{
			OutputStream outToServer = client.getOutputStream();
	    	DataOutputStream out = new DataOutputStream(outToServer);
	    	out.writeUTF("Get_Friends "+username);
	    	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
        	
        	String msg = in.readUTF();
        	String[] strArray = msg.split("\\s+");
        	return strArray;
	    	
		}
		catch(IOException e) {
		 	   e.printStackTrace();
		 }
		return null;
	}
	
	public String getChatLog(String user1, String user2) {
		String [] chatLog= null;
		String msg = "";
		try {
			OutputStream outToServer = client.getOutputStream();
	    	DataOutputStream out = new DataOutputStream(outToServer);
	    	out.writeUTF("Get_Chat "+user1+" "+user2);
	    	
	    	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
	    	msg = in.readUTF();
	    	//chatLog = msg.split("\\s+");
    	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public void sendMessage(String sender, String reciever, String msg) {
		try {
			OutputStream outToServer = client.getOutputStream();
	    	DataOutputStream out = new DataOutputStream(outToServer);
	    	out.writeUTF("Send_MSG "+sender+" "+reciever+" "+msg);
	    	
	    	InputStream inFromServer = client.getInputStream();
        	DataInputStream in = new DataInputStream(inFromServer);
    	
    	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
}
