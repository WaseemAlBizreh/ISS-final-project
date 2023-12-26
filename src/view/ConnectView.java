package view;

import api.ClientSocket;
import api.Encryption;
import api.Operation;
import model.LoginRegisterModel;
import model.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ConnectView {
    private JFrame frame;
    private JTextField serverIPField;
    private JTextField serverPortField;

    private final ClientSocket clientSocket;

    public ConnectView(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        buildScreen();
    }

    private void connectToServer() {
        String serverIP = serverIPField.getText();
        String serverPortText = serverPortField.getText();

        // Check for missing data
        if (serverIP.isEmpty() || serverPortText.isEmpty()) {
            // Show a notification if there is missing data
            JOptionPane.showMessageDialog(frame, "Please enter both Server IP and Server Port.",
                    "Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int serverPort = Integer.parseInt(serverPortText);

            // Call the connectToServer method in the client controller
            boolean connect = clientSocket.connectToServer(serverIP, serverPort);
            if (connect) {
                JOptionPane.showMessageDialog(frame, "You Connect with Server Successfully.",
                        "Connect Successfully", JOptionPane.INFORMATION_MESSAGE);
            }

            // You can perform other actions after successful connection, such as showing the next screen
            //TODO: Navigate To Next Screen
            //TODO: Write Code Here

            //Send Test Message
            try {
                LoginRegisterModel body = new LoginRegisterModel("waseem", "pass");
                Message request = new Message(body, Operation.Login);
                // Send Message
                Message response = clientSocket.sendMessageToServer(request, Encryption.AES);
                //Print Server Response
                System.out.println("Server: " + response);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException | IOException e) {
            // Handle the case where the port is not a valid integer
            JOptionPane.showMessageDialog(frame, "Please enter a valid Server Port.",
                    "Invalid Port", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buildScreen() {
        frame = new JFrame("Connect View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(null);

        JLabel serverIPLabel = new JLabel("Server IP:");
        serverIPLabel.setBounds(30, 20, 80, 30);

        serverIPField = new JTextField();
        serverIPField.setBounds(130, 20, 170, 30);

        JLabel serverPortLabel = new JLabel("Server Port:");
        serverPortLabel.setBounds(30, 60, 80, 30);

        serverPortField = new JTextField();
        serverPortField.setBounds(130, 60, 170, 30);

        JButton connectButton = new JButton("Connect with Server");
        connectButton.setBounds(70, 110, 200, 30);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        frame.add(serverIPLabel);
        frame.add(serverIPField);
        frame.add(serverPortLabel);
        frame.add(serverPortField);
        frame.add(connectButton);

        frame.setVisible(true);
    }
}

