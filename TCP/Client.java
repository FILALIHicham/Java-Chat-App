package chatApp.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	// Socket for communication with the server
	private Socket client;
	
	// Reader for incoming messages
	public BufferedReader in;
	
	// Writer for outgoing messages
	public PrintWriter out;
	
	// Flag to indicate if the client is done communicating
	public boolean done;
	
	// Constructor that creates a new Socket and establishes input and output streams
	public Client() {
		try {
			// Connect to the server at IP address 127.0.0.1 and port 10100
			client = new Socket("127.0.0.1", 10100);
			
			// Create a PrintWriter for sending messages to the server
			out = new PrintWriter(client.getOutputStream(), true);
			
			// Create a BufferedReader for receiving messages from the server
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
		} catch (IOException e) {
			// If there was an error creating the socket, shutdown the client
			shutdown();
		}
	}
	
	// Method for shutting down the client and closing its resources
	public void shutdown() {
		done = true;
		try {
			// Close the input stream
			in.close();
			
			// Close the output stream
			out.close();
			
			// If the socket is still open, close it
			if(!client.isClosed()) {
				client.close();
			}
		} catch (IOException e) {
			// ignore exceptions while shutting down
		}
	}
}
