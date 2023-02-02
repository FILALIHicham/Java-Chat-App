package chatApp.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Class `Server` implements the `Runnable` interface, allowing the server to be run in a separate thread.
public class Server implements Runnable{

	// `connections` is an array list to store all the connected clients
	private ArrayList<ConnectionHandler> connections;
	// `server` is a `ServerSocket` to listen for incoming connections
	private ServerSocket server;
	// `done` is a flag to check if the server should stop listening
	private boolean done;
	// `pool` is an `ExecutorService` to handle concurrent connections
	private ExecutorService pool;
	
	// Constructor for the `Server` class, creates a new array list for connections
	public Server() {
		connections = new ArrayList<>();
		done = false;
	}
	
	// Overridden `run` method from the `Runnable` interface
	@Override
	public void run() {
		try {
			// Create a new `ServerSocket` on port 10100
			server = new ServerSocket(10100);

			// Creates a new `ExecutorService` with an unbounded thread pool
			pool = Executors.newCachedThreadPool();
			while (!done) {
				// The `server` listens for incoming connections and accepts the connection
				Socket client = server.accept();
				// A new `ConnectionHandler` is created for the incoming connection
				ConnectionHandler handler = new ConnectionHandler(client);
				// The `ConnectionHandler` is added to the list of connections
				connections.add(handler);	
				// The `ConnectionHandler` is executed in a separate thread by the `ExecutorService`
				pool.execute(handler);
			}

		}
		// Catches any `Exception` that may occur and shuts down the server
		catch (Exception e) {
			shutdown();
		}
	}
	
	// method to broadcast a message to all connected clients
	public void broadcast(String message) {
		// loops through all the connections and sends the message to each client
		for (ConnectionHandler ch : connections) {
			if (ch != null) {
				ch.sendMessage(message);
			}
		}
	}
	
	// method to shutdown the server and close all connections.
	public void shutdown(){
		try {
			done = true;
			pool.shutdown();
			if (!server.isClosed()) {
				server.close();
			}
			for (ConnectionHandler ch : connections) {
				ch.shutdown();
			}
		} catch (IOException e) {
			// ignore
		}
	}
	
	// Class `ConnectionHandler` implements the `Runnable` interface, allowing the connection to be run in a separate thread.
	class ConnectionHandler implements Runnable{

		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		private String pseudo;
		
		// The constructor takes a socket representing the client and initializes the input and output streams.
		public ConnectionHandler(Socket client) {
			this.client = client;
		}

		// The run method reads the client's pseudo and broadcasts a message to all
		// clients indicating that the client has joined the chat.
		// Then, it reads messages from the client and broadcasts them to all clients,
		// unless the message starts with "/pseudo" or "/exit".
		// If the message starts with "/pseudo", the client's pseudo is changed, and if
		// it starts with "/exit", the client's connection is closed.
		@Override
		public void run() {
			try {
				// Initialize the input and output streams
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				// Read the client's pseudo
				pseudo = in.readLine();
				System.out.println(pseudo + " connected!");
				// Broadcast a message to all clients indicating that the client has joined the chat
				broadcast(pseudo + " joined the chat!");
				String message;
				// Continuously read messages from the client
				while((message = in.readLine()) != null ){
					if (message.startsWith("/pseudo ")) {
						// If the message starts with "/pseudo", change the client's pseudo
						String[] messageSplit = message.split(" ", 2);
						if(messageSplit.length==2) {
							// Broadcast a message to all clients indicating that the client has changed their pseudo
							broadcast(pseudo+" renamed themselves to "+ messageSplit[1]);
							pseudo = messageSplit[1];
							out.println("Successfully changed pseudo to "+ pseudo);
						} else {
							// If no pseudo is provided, send an error message to the client
							out.println("No pseudo provided!");
						}
					}else if(message.startsWith("/exit")) {
						// If the message starts with "/exit", close the client's connection
						broadcast(pseudo + " left the chat!");
						shutdown();
					}else {
						// Otherwise, broadcast the message to all clients
						broadcast(pseudo + ": "+ message);
					}
				}
			} catch (Exception e) {
				// If an exception is thrown, shutdown the connection handler
				shutdown();
			}
			
		}

		// method to send a message to the client
		public void sendMessage(String message) {
			out.println(message);
		}
		
		// method to shutdown the connection handler
		public void shutdown() {
			try {
				in.close();
				out.close();
				if (!client.isClosed()) {
					client.close();
				}				
			} catch (IOException e) {
				//ignore
			}
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
}
