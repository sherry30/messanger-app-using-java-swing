package controller;
import logic.Client;

public class ClientController {
	private static Client c = new Client();
	
	private ClientController () {};
	
	public static Client getInstance(){
	      return c;
	}
}
