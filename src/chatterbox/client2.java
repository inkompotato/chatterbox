package chatterbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class client2 {

	public static void main(String[] args) {
		if (args.length == 3) {
			new client2().run(args[0], Integer.parseInt(args[1]), args[2]);
		} else {
			System.out.println("Please use the following arguments: serverIP, serverPort, UserNickname");
		}

	}

	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;

	private void run(String address, int port, String nickname) {
		try {			
			
			connectToServer(address, port);
			setupStreams();
			activeConversation(nickname);
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

	private void activeConversation(String nickname) throws IOException {
		String message;
		String info = nickname;
		
		
		new Thread() {
		Scanner scanner = new Scanner(System.in);
		
			@Override
			public void run() {
				String messageToSend;
				output.println(info);
			do {
				messageToSend = scanner.nextLine();
				if (!(messageToSend.trim().isEmpty())){
					output.println(messageToSend);
				}
			} while (!(messageToSend.equals("/quit")));
			scanner.close();
			
			}
		}.start();
		
				
		while ((message = input.readLine()) != null) {
			System.out.println(message);
		}
		
		
		
	}

	private void connectToServer(String address, int port) throws IOException {
		socket = new Socket(address, port);
		System.out.println("Connecting to a Server \n IP: " + address + " \n Port: " + port);
	}

	private void setupStreams() throws IOException {
		output = new PrintWriter(socket.getOutputStream(), true);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

}
