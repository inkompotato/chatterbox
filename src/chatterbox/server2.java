package chatterbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class server2 {

	public static void main(String[] args) {
		new server2().run(Integer.parseInt(args[0]));
	}

	private ServerSocket server;
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	static HashSet<ClientHandler> clients = new HashSet<ClientHandler>();

	private void run(int port) {

		try {
			System.out.println("Server started succesfully!");
			server = new ServerSocket(port);

			while (true) {
				
				waitforConnection();
				setupStreams();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void waitforConnection() throws IOException {
		socket = server.accept();
		System.out.println("A new user joined the server");
	}

	private void setupStreams() throws IOException {
		output = new PrintWriter(socket.getOutputStream(), true);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		
		String info = checkNick(input.readLine());
		
		output.println("You are now connected as "+info);
		ClientHandler mtch = new ClientHandler(socket,info, input, output);
		Thread t = new Thread(mtch);
		System.out.println("Adding "+info+" to active client list ["+(clients.size()+1)+"]...");
		clients.add(mtch);
		t.start();
	}
	
	static void broadcast(String metamessage, String sender) {
		for (ClientHandler c: clients) {
			if (!(c.getNickname().equals(sender))) {
				c.distribute(metamessage);
			}
		}
	}
	
	private String checkNick(String info) {
		for (ClientHandler c: clients) {
			if(c.getNickname().equals(info)) {
				info= info+""+(int)(Math.random()*10);
				checkNick(info);
			}
		}
		return info;
	}

}
