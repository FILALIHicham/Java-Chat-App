package chatApp.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// MessageSender class implements Runnable to send messages over a Datagram socket
class MessageSender implements Runnable {
	// `socket` is a `DatagramSocket` to send messages to the server
	private DatagramSocket socket;
	// `hostName` is the name of the host running the server
	private String hostName;
	// `window` is the GUI window
	private GUI window;
	// `nickname` is the client's nickname
	private String nickname;

	// Constructor to initialize the class variables
	MessageSender(DatagramSocket sock, String host, GUI win, String name) {
		socket = sock;
		hostName = host;
		window = win;
		nickname = name;
	}

	// Method to send a message over the Datagram socket
	private void sendMessage(String s) throws Exception {
		s = nickname + ": " + s; // add the nickname to the message
		byte buffer[] = s.getBytes(); // convert the message to a byte array
		InetAddress address = InetAddress.getByName(hostName); // get the address of the host
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 10100); // create a new Datagram
																							// packet
		socket.send(packet); // send the packet
	}

	// Overriding run method to send messages
	public void run() {
		boolean connected = false;
		do {
			try {
				sendMessage("Connected - welcome!"); // send a welcome message
				connected = true;
			} catch (Exception e) {
				// If there is an exception, display the error message on the GUI window
				window.displayMessage(e.getMessage());
			}
		} while (!connected);
		// Keep sending messages until the window is closed
		while (true) {
			try {
				while (!window.message_is_ready) {
					Thread.sleep(100); // Wait for 100ms if the message is not ready
				}
				sendMessage(window.getMessage()); // send the message
				window.setMessageReady(false); // Resetting the message_is_ready flag
			} catch (Exception e) {
				// If there is an exception, display the error message on the GUI window
				window.displayMessage(e.getMessage());
			}
		}
	}
}

// MessageReceiver class implements Runnable to receive messages over a Datagram socket
class MessageReceiver implements Runnable {
	DatagramSocket socket; // `socket` is a `DatagramSocket` to receive messages from the server
	byte buffer[]; // `buffer` is a byte array to store the received message
	GUI window; // `window` is the GUI window

	// Constructor to initialize the class variables
	MessageReceiver(DatagramSocket sock, GUI win) {
		socket = sock;
		buffer = new byte[1024];
		window = win;
	}

	// Overriding run method to receive messages
	public void run() {
		// Keep receiving messages until the window is closed
		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // create a new Datagram packet
				socket.receive(packet); // receive the packet
				String received = new String(packet.getData()); // convert the packet to a string
				window.displayMessage(received); // display the message on the GUI window
			} catch (Exception e) {
				// If there is an exception, display the error message on the GUI window
				System.err.println(e);
			}
		}
	}
}

// Client class to create a GUI window and start the MessageSender and MessageReceiver threads
public class Client {

	// Main method to start the client
	public static void main(String args[]) throws Exception {

		// Create a new GUI window
		GUI window = new GUI();
		// Get the client's nickname
		String name = window.getNickname();
		// If the nickname is null, exit the program
		if (name == null) {
			System.exit(0);
		}
		// Set the title of the window to the nickname
		window.setTitle("UDP Chat - " + name);
		DatagramSocket socket = new DatagramSocket(); // Create a new Datagram socket
		MessageReceiver receiver = new MessageReceiver(socket, window); // Create a new MessageReceiver object
		MessageSender sender = new MessageSender(socket, "127.0.0.1", window, name); // Create a new MessageSender
																						// object
		Thread receiverThread = new Thread(receiver); // Create a new thread for the MessageReceiver object
		Thread senderThread = new Thread(sender); // Create a new thread for the MessageSender object
		receiverThread.start(); // Start the receiver thread
		senderThread.start(); // Start the sender thread
	}
}
