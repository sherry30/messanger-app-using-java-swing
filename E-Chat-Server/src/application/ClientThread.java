package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ClientThread extends Thread{
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean connected;
	
	public ClientThread (Socket c) {
		
		client = c;
		try {
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
		}
		catch (Exception e) { 
		System.out.println (e); 
		}
	}
	
	public void run() {
		try { 
			while (true) { 
				String msg = in.readUTF(); 
				System.out.println (msg);
				System.out.println (ConnectionServer.clients.size());
				String[] strArray = msg.split("\\s+");
				
				//if exit request recieved
				if(strArray[0].equals("exit")) {
					if(strArray.length>1) {
						Statement stmt2 = ConnectionServer.con.createStatement();
						int res=stmt2.executeUpdate("update echat.users set is_online=0 where username ='"+strArray[1] +"'");
					}
					break;
				}
				
				//if Login request is recieved
				else if (strArray[0].equals("Login")) {
					Statement stmt = ConnectionServer.con.createStatement(); 
					
					Cipher cipher = Cipher.getInstance("AES");
					cipher.init(Cipher.ENCRYPT_MODE, ConnectionServer.aesKey);
		            byte[] encrypted = cipher.doFinal(strArray[2].getBytes());
		            String pass = new String(encrypted);
		            
					ResultSet rs=stmt.executeQuery("SELECT * FROM echat.users WHERE username = '" + strArray[1] + "' AND password = '" + pass + "'");
					
					if (rs.next()) {
						System.out.println("found");
						out.writeUTF("True");
						
						//updating the user as online
						Statement stmt2 = ConnectionServer.con.createStatement();
						int res=stmt2.executeUpdate("update echat.users set is_online=1 where username ='"+strArray[1] +"'");
						
					}
					else {
						System.out.println("not found");
						out.writeUTF("False");
					}
				}
				
				//if signUp request is recieved
				else if(strArray[0].equals("SignUp")) {
					Statement stmt = ConnectionServer.con.createStatement();  
					ResultSet rs=stmt.executeQuery("SELECT * FROM echat.users WHERE username = '" + strArray[1]+ "'");
					
					//if this username already exists
					if(rs.next()) {
						System.out.println("User already exists");
						out.writeUTF("exist:username");
					}
					//else create the user
					else {
						Cipher cipher = Cipher.getInstance("AES");
						cipher.init(Cipher.ENCRYPT_MODE, ConnectionServer.aesKey);
			            byte[] encrypted = cipher.doFinal(strArray[2].getBytes());
			            String pass = new String(encrypted);
			            
						Statement stmt2 = ConnectionServer.con.createStatement();  
						int res=stmt2.executeUpdate("Insert into echat.users (username, password, email, FirstName, LastName) values ('" + strArray[1]+ "'" + " , "+ "'" + pass+ "'" + " , " +"'" + strArray[3]+ "'" + " , " +"'" + strArray[4]+ "'" + " , " +"'" + strArray[5]+ "')");
						if(res>0)
							out.writeUTF("True");
						else
							out.writeUTF("error");
					}
				}
				
				//if user logsout
				else if(strArray[0].equals("Logout")) {
					//updating the user as offline
					Statement stmt2 = ConnectionServer.con.createStatement();
					int res=stmt2.executeUpdate("update echat.users set is_online=0 where username ='"+strArray[1] +"'");
					if(res>0) {
						out.writeUTF("True");
						//break;
					}
					else
						out.writeUTF("error");
				}
				
				//for searching friend
				else if(strArray[0].equals("Search_Friend")) {
					Statement stmt = ConnectionServer.con.createStatement();
					ResultSet rs=stmt.executeQuery("SELECT * FROM echat.users WHERE username = '" + strArray[1]+ "'");
					
					if(rs.next()) {
						out.writeUTF("True");
					}
					else {
						out.writeUTF("False");
					}
				}
				
				//for sending request
				else if(strArray[0].equals("Add_Friend")) {
					Statement stmt = ConnectionServer.con.createStatement();
					//ResultSet rs=stmt.executeQuery("SELECT * FROM echat.users WHERE username = '" + strArray[1]+ "'");
					int res=stmt.executeUpdate("insert into echat.FriendRequest (userID,requestFrom) values ((select userID from users where username='" + strArray[1]+"') , (select userID from users where username='" + strArray[2]+"'))");
					
				}
				
				//for getting allthe friend requests
				else if(strArray[0].equals("Get_Requests")) {
					Statement stmt = ConnectionServer.con.createStatement();
					ResultSet rs=stmt.executeQuery("select users.* from users inner join (select FriendRequest.* from FriendRequest inner join users on users.userID = FriendRequest.userID where users.username='" + strArray[1]+"') as subquery on subquery.RequestFrom=users.userID where subquery.Accepted IS NULL");
					int n=0;
					String temp = "";
					String msg2="";
					
					while(rs.next()) {
						n++;
						temp+=rs.getString("username");
						temp+=" ";
					}
					msg2=Integer.toString(n)+" "+temp;
					System.out.println(msg2);
					out.writeUTF(msg2);
					
				}
				
				//for accpeting requests
				else if(strArray[0].equals("Accept_Requests")){
					Statement stmt = ConnectionServer.con.createStatement();
					int res=stmt.executeUpdate("update echat.FriendRequest SET Accepted=1 where FriendRequestID IN (select* from(select echat.subquery.FriendRequestID from (select echat.FriendRequest.* from echat.FriendRequest inner join echat.users on echat.FriendRequest.userID = echat.users.userID where echat.users.username='"+strArray[1]+"') as subquery where subquery.RequestFrom IN (select echat.FriendRequest.RequestFrom from echat.FriendRequest inner join echat.users on echat.FriendRequest.RequestFrom = echat.users.userID where echat.users.username='"+strArray[2]+"'))as tempTable1)");
					Statement stmt2 = ConnectionServer.con.createStatement();
					stmt2.executeUpdate("insert into echat.FriendList(userID,friendsWith) values((select userID from echat.users where username='"+strArray[1]+"'),(select userID from echat.users where username = '"+strArray[2]+"'))");
					/*if(res>0) {
						System.out.println("Worked");
					}
					else {
						System.out.println("Didnt work");
					}*/
				}
				
				//for getting all the friends
				else if(strArray[0].equals("Get_Friends")) {
					String msg2="";
					Statement stmt = ConnectionServer.con.createStatement();
					ResultSet rs=stmt.executeQuery("select echat.users.* from users inner join (select echat.FriendList.* from echat.FriendList inner join echat.users on echat.users.userID = echat.FriendList.userID where echat.users.username='"+strArray[1]+"') as subquery on subquery.friendsWith = echat.users.userID");
					while(rs.next()) {
						msg2+=rs.getString("FirstName")+" "+rs.getString("LastName")+" "+rs.getByte("is_online")+" ";
					}
					Statement stmt2 = ConnectionServer.con.createStatement();
					ResultSet rs2=stmt2.executeQuery("select echat.users.* from users inner join (select echat.FriendList.* from echat.FriendList inner join echat.users on echat.users.userID = echat.FriendList.friendsWith where echat.users.username='"+strArray[1]+"') as subquery on subquery.userID = echat.users.userID");
					while(rs2.next()) {
						msg2+=rs2.getString("FirstName")+" "+rs2.getString("LastName")+" "+rs2.getByte("is_online")+" ";
					}
					System.out.println(msg2);
					out.writeUTF(msg2);
					
					
				}
				else if(strArray[0].equals("Get_Chat")) {
					String firstName = strArray[2];
					String lastName = strArray[3];
					String username1 = strArray[1];
					Statement stmt = ConnectionServer.con.createStatement();
					ResultSet rs=stmt.executeQuery("select * from users where FirstName='"+firstName+"' AND  LastName = '"+lastName+"'");
					rs.next();
					String username2 = rs.getString("username");
					rs=stmt.executeQuery("select * from FriendList where (userID IN (select userID from users where username = '"+username1+"') AND friendsWith = (select userID from users where username = '"+username2+"')) OR (userID IN (select userID from users where username = '"+username2+"') AND friendsWith = (select userID from users where username ='"+username1+"'))");
					rs.next();
					int chatID = rs.getInt("chatID");
					rs=stmt.executeQuery("select * from chat where chatID = "+chatID);
					String temp = "";
					while(rs.next()) {
						temp+="sender,"+rs.getString("sender")+","+"reciever,"+rs.getString("reciever")+",msg,"+rs.getString("message")+",";
					}
					out.writeUTF(temp);
				}
				
				else if(strArray[0].equals("Send_MSG")) {
					String firstName = strArray[2];
					String lastName = strArray[3];
					String sender = strArray[1];
					String tempMsg="";
					for(int i=4;i<strArray.length;i++) {
						tempMsg+=strArray[i]+" ";
					}
					Statement stmt = ConnectionServer.con.createStatement();
					ResultSet rs=stmt.executeQuery("select * from users where FirstName='"+firstName+"' AND  LastName = '"+lastName+"'");
					rs.next();
					String reciever = rs.getString("username");
					
					rs=stmt.executeQuery("select * from FriendList where (userID IN (select userID from users where username = '"+sender+"') AND friendsWith = (select userID from users where username = '"+reciever+"')) OR (userID IN (select userID from users where username = '"+reciever+"') AND friendsWith = (select userID from users where username ='"+sender+"'))");
					rs.next();
					int chatID = rs.getInt("chatID");
					
					int res=stmt.executeUpdate("insert into chat(sender, reciever, chatID, message) values('"+sender+"','"+reciever+"',"+chatID+",'"+tempMsg+"')");
					
					
				}
				
				
			}
		}																																																															
		catch (Exception e) 
		{ 
			System.out.println("This one called");
			System.out.println (e); 
		}
	}
}
