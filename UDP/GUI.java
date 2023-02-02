package chatApp.UDP;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;


@SuppressWarnings("serial")
public class GUI extends JFrame {

	String host_name;
	String nickname;
	JTextPane message_field;
	private JTextArea chatArea;
	private JButton jButton;
	private JPanel jPanel;
	private JScrollPane jScrollPane;
	private JTextField jTextField;
	
	String message = "";
	boolean message_is_ready = false;

	public GUI() {
		
		JDialog clientNameDialog = new JDialog(this, "Nickname", true);
		clientNameDialog.getContentPane().setBackground( new java.awt.Color(39, 52, 67));
		JLabel label = new JLabel("Enter your nickname: ");
		label.setForeground(Color.WHITE);
		JTextField nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(100,20));
		JButton ok = new JButton("OK");
		ok.setBackground(new java.awt.Color(30, 190, 165));
		clientNameDialog.setLayout(new FlowLayout());
		clientNameDialog.add(label);
		clientNameDialog.add(nameField);
		clientNameDialog.add(ok);
		clientNameDialog.setLocationRelativeTo(null);
		clientNameDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		clientNameDialog.setSize(250, 100);
		clientNameDialog.setResizable(false);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nickname = nameField.getText().trim();
				clientNameDialog.dispose();
			}
		});
		clientNameDialog.setVisible(true);
		
		jPanel = new javax.swing.JPanel();
		jTextField = new javax.swing.JTextField();
		jButton = new javax.swing.JButton();
		jScrollPane = new javax.swing.JScrollPane();
		chatArea = new javax.swing.JTextArea();
		
		setTitle("UDP Chat room");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		jPanel.setBackground(new java.awt.Color(39, 52, 67));
		jPanel.setForeground(new java.awt.Color(204, 204, 204));
		jPanel.setLayout(null);

		jPanel.add(jTextField);
		jTextField.setBounds(10, 370, 390, 30);

		jButton.setBackground(new java.awt.Color(30, 190, 165));
		jButton.setText("Send");
		jButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
					message = jTextField.getText().trim();
					jTextField.setText(null);
					if (!message.equals(null) && !message.equals("")) {
						message_is_ready = true;
					}
				}
		});

		jTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == 10) {
					jTextField.setCaretPosition(0);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == 10 && !message_is_ready) {
					message = jTextField.getText().trim();
					jTextField.setText(null);
					if (!message.equals(null) && !message.equals("")) {
						message_is_ready = true;
					}
				}
			}
		});
		jPanel.add(jButton);
		jButton.setBounds(410, 370, 80, 30);

		chatArea.setColumns(20);
		chatArea.setRows(5);
		chatArea.setEditable(false);
		jScrollPane.setViewportView(chatArea);

		jPanel.add(jScrollPane);
		jScrollPane.setBounds(10, 10, 480, 350);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel));

		setSize(new java.awt.Dimension(520, 450));
		setLocationRelativeTo(null);
		
		
		
		
		////////////////
		setVisible(true);
		jTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == 10) {
					jTextField.setCaretPosition(0);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == 10 && !message_is_ready) {
					message = jTextField.getText().trim();
					jTextField.setText(null);
					if (!message.equals(null) && !message.equals("")) {
						message_is_ready = true;
					}
				}
			}
		});
	}

	public void displayMessage(String receivedMessage) {
		chatArea.append(receivedMessage + "\n");
	}

	public boolean isMessageReady() {
		return message_is_ready;
	}

	public void setMessageReady(boolean messageReady) {
		this.message_is_ready = messageReady;
	}

	public String getMessage() {
		return message;
	}

	public String getHostName() {
		return host_name;
	}
	public String getNickname() {
		return nickname;
	}
}

