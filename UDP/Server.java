package chatApp.UDP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable {

	private DatagramSocket socket; // Create a Datagram socket to listen on port 10100
	private ArrayList<InetAddress> client_addresses; // Keep track of client addresses
	private ArrayList<Integer> client_ports; // Keep track of client ports
	private HashSet<String> existing_clients; // Keep track of existing clients

	// Suppress warnings for raw and unchecked types
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Server() throws IOException {
		// Create a Datagram socket to listen on port 10100
		socket = new DatagramSocket(10100);
		System.out.println("Server is running on port " + 10100);
		// Initialize the ArrayLists and HashSet
		client_addresses = new ArrayList();
		client_ports = new ArrayList();
		existing_clients = new HashSet();
	}

	public void run() {
		byte[] buffer = new byte[1024]; // Create a buffer to store the received messages
		// loop indefinitely to receive messages
		while (true) {
			try {
				Arrays.fill(buffer, (byte) 0); // Fill the buffer with 0s
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // Create a new Datagram packet
				socket.receive(packet); // Receive the packet

				// Convert the received data into a string
				String message = new String(buffer, 0, buffer.length);

				// Get the client address and port from the received data
				InetAddress clientAddress = packet.getAddress();
				int client_port = packet.getPort();

				// Construct a unique ID for the client based on their address and port
				String id = clientAddress.toString() + "|" + client_port;

				// If the client has not been seen before, add them to the list of existing
				// clients
				if (!existing_clients.contains(id)) {
					existing_clients.add(id);
					client_ports.add(client_port);
					client_addresses.add(clientAddress);
				}

				// Print the received message
				System.out.println(message);
				// Convert the message to bytes
				byte[] data = (message).getBytes();
				// Send the message to all clients
				for (int i = 0; i < client_addresses.size(); i++) {
					InetAddress cl_address = client_addresses.get(i); // Get the client address
					int cl_port = client_ports.get(i); // Get the client port
					packet = new DatagramPacket(data, data.length, cl_address, cl_port); // Create a new Datagram packet
					socket.send(packet); // Send the packet
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	// Main method
	public static void main(String args[]) throws Exception {
		Server server_thread = new Server(); // Create a new Server thread
		server_thread.run(); // Start the Server thread
	}
}