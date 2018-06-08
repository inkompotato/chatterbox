package chatterbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
	
	private String nickname;
	Scanner scn = new Scanner(System.in);
    final BufferedReader input;
    final PrintWriter output;
    Socket socket;

    
	public ClientHandler(Socket socket, String info, BufferedReader input, PrintWriter output) {
		this.output = output;
		this.input = input;
		this.nickname = info;
		this.socket = socket;

	}

	@Override
	public void run() {
		try {
			output.println("Welcome to the Chatterbox. Enjoy your stay :-)");
			server2.broadcast(nickname+" connected...", nickname);
			String message;
			do {
				message = input.readLine();
				String metamessage = nickname+": "+message;
				System.out.println(metamessage);
				server2.broadcast(metamessage, nickname);
			} while (!(message.equals("/quit")));
			
			System.out.println(nickname+ " disconnected...");
			output.println("disconnected...");
			server2.broadcast(nickname+" disconnected...", nickname);
			
			System.out.println("Removing "+ nickname+ " from active client list ["+(server2.clients.size()-1)+"]...");
			server2.clients.remove(this);
			
			socket.close();
		} catch (IOException e) {
			
			System.out.println(nickname+ " dropped connection...");
			server2.broadcast(nickname+" dropped connection...", nickname);
			server2.clients.remove(this);
			//e.printStackTrace();
		} 
		
		
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void distribute(String metamessage) {
		output.println(metamessage);
	}

}
