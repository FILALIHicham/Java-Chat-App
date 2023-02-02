package chatApp.TCP;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JTextArea chatArea;
	private javax.swing.JButton jButton;
	private javax.swing.JPanel jPanel;
	private javax.swing.JScrollPane jScrollPane;
	private javax.swing.JTextField jTextField;
	private Client client;
	private String nickname;

	public static void main(String[] args) {
		// Create a new client object
		Client client = new Client();

		// Create a new GUI object and pass the client object to it
		// The @SuppressWarnings("unused") is used to suppress the unused warning for
		// the GUI object
		@SuppressWarnings("unused")
		GUI gui = new GUI(client);
	}

	public GUI(Client client) {
		// Initialize the GUI components
		initComponents();

		// Store the client object as a member of this GUI object
		this.client = client;

		// Call the nickname method
		nickname();

		// Create a new thread to handle incoming messages from the client
		Thread outputThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Store the incoming message
				String inMessage;

				try {
					// Read incoming messages from the client's input stream until it is null
					while ((inMessage = client.in.readLine()) != null) {
						// Append the incoming message to the chat area
						chatArea.append(inMessage + "\n");
					}
				} catch (IOException e) {
					// Print the stack trace of the exception if there is any error
					e.printStackTrace();
				}
			}
		});

		// Start the output thread
		outputThread.start();

		// Set the title of the GUI window to "TCP Chat - [nickname]"
		this.setTitle("TCP Chat - " + nickname);

		// Make the GUI window visible
		this.setVisible(true);
	}

	private void nickname() {
		// Create a dialog window with title "Nickname" and modal set to true
		JDialog clientNameDialog = new JDialog(this, "Nickname", true);
		// Set the background color of the dialog window
		clientNameDialog.getContentPane().setBackground(new java.awt.Color(39, 52, 67));

		// Create a label with text "Enter your nickname: "
		JLabel label = new JLabel("Enter your nickname: ");
		// Set the color of the text to white
		label.setForeground(Color.WHITE);

		// Create a text field for entering the nickname
		JTextField nameField = new JTextField();
		// Set the preferred size of the text field
		nameField.setPreferredSize(new Dimension(100, 20));

		// Create a button with text "OK"
		JButton ok = new JButton("OK");
		// Set the background color of the button
		ok.setBackground(new java.awt.Color(30, 190, 165));

		// Set the layout of the dialog window to FlowLayout
		clientNameDialog.setLayout(new FlowLayout());

		// Add the label, text field, and button to the dialog window
		clientNameDialog.add(label);
		clientNameDialog.add(nameField);
		clientNameDialog.add(ok);

		// Set the location of the dialog window relative to the center of the screen
		clientNameDialog.setLocationRelativeTo(null);
		// Set the default close operation to dispose of the dialog window
		clientNameDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// Set the size of the dialog window
		clientNameDialog.setSize(250, 100);
		// Set the dialog window to be non-resizable
		clientNameDialog.setResizable(false);

		// Add an action listener to the OK button
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Set the nickname to the text entered in the text field
				nickname = nameField.getText().trim();
				// Send the nickname to the server
				client.out.println(nameField.getText().trim());
				// Dispose of the dialog window
				clientNameDialog.dispose();
			}
		});

		// Make the dialog window visible
		clientNameDialog.setVisible(true);
	}

	/**
	 * This method initializes the components for the chat window, including the
	 * text input field, send button, chat area, and the panel that contains all of
	 * these components.
	 */
	private void initComponents() {
		// Creates a JPanel
		jPanel = new javax.swing.JPanel();
		// Creates a JTextField for text input
		jTextField = new javax.swing.JTextField();
		// Creates a JButton for sending messages
		jButton = new javax.swing.JButton();
		// Creates a JScrollPane for the chat area
		jScrollPane = new javax.swing.JScrollPane();
		// Creates a JTextArea for displaying messages
		chatArea = new javax.swing.JTextArea();

		// Sets the default close operation of the window to exit the program
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		// Sets the resizability of the window to false
		setResizable(false);

		// Sets the background color of the JPanel
		jPanel.setBackground(new java.awt.Color(39, 52, 67));
		// Sets the foreground color of the JPanel
		jPanel.setForeground(new java.awt.Color(204, 204, 204));
		// Sets the layout of the JPanel to null
		jPanel.setLayout(null);

		// Adds the JTextField to the JPanel
		jPanel.add(jTextField);
		// Sets the bounds of the JTextField
		jTextField.setBounds(10, 370, 390, 30);

		// Sets the background color of the JButton
		jButton.setBackground(new java.awt.Color(30, 190, 165));
		// Sets the text of the JButton
		jButton.setText("Send");
		// Adds an action listener to the JButton
		jButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// Gets the text from the JTextField
				String message = jTextField.getText();
				// If the message is "/exit", closes the window, disposes the window, and shuts
				// down the client
				if (message.equals("/exit")) {
					client.out.println(message);
					setVisible(false);
					dispose();
					client.shutdown();
				}
				if (message.startsWith("/pseudo ")) {
					// Send the message to the client output
					client.out.println(message);

					// Clear the text field
					jTextField.setText("");

					// Split the message by spaces into an array
					String[] messageSplit = message.split(" ", 2);

					// Check if the split array has length 2 (meaning it contains the command and a
					// nickname)
					if (messageSplit.length == 2) {
						// Set the nickname to the second element in the split array
						nickname = messageSplit[1];

						// Update the window title to include the nickname
						setTitle("TCP Chat - " + nickname);
					}
				} else {
					// If the message doesn't start with "/pseudo", send it to the client output
					client.out.println(message);

					// Clear the text field
					jTextField.setText("");
				}
			}
		});

		jTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// If the key pressed is the Enter key
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Get the text from the text field
					String message = jTextField.getText();
					// If the message is "/exit"
					if (message.equals("/exit")) {
						// Send the message to the server
						client.out.println(message);
						// Hide the window
						setVisible(false);
						// Close the window
						dispose();
						// Close the client
						client.shutdown();
					}
					// If the message starts with "/pseudo "
					if (message.startsWith("/pseudo ")) {
						// Split the message into two parts, using " " as the separator
						String[] messageSplit = message.split(" ", 2);
						// If the split message has two parts
						if (messageSplit.length == 2) {
							// Update the nickname
							nickname = messageSplit[1];
							// Update the window title to include the nickname
							setTitle("TCP Chat - " + nickname);
							// Send the message to the server
							client.out.println(message);
							// Clear the text field
							jTextField.setText("");
						}
					} else {
						// Send the message to the server
						client.out.println(message);
						// Clear the text field
						jTextField.setText("");
					}
				}
			}
		});

		// Add the button to the panel
		jPanel.add(jButton);
		// Set the button's position and size
		jButton.setBounds(410, 370, 80, 30);

		// Set the chat area to be uneditable and add it to the scroll pane
		chatArea.setColumns(20);
		chatArea.setRows(5);
		chatArea.setEditable(false);
		jScrollPane.setViewportView(chatArea);

		// Add the scroll pane to the panel
		jPanel.add(jScrollPane);
		// Set the scroll pane's position and size
		jScrollPane.setBounds(10, 10, 480, 350);

		// Set the layout of the content pane
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel));

		// Set the window's size and location
		setSize(new java.awt.Dimension(520, 450));
		setLocationRelativeTo(null);
	}

}
