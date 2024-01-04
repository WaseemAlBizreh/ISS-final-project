package view;

import api.ClientSocket;
import app.Utils;
import model.DigitalCertificate;
import model.Message;
import security.JavaPGP;
import security.SessionKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class EquationUI {
    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;
    private final ClientSocket clientSocket;
    private final KeyPair keys;
    private final SessionKey sessionKey;
    String equation;

    public EquationUI(String equation, ClientSocket clientSocket, KeyPair keys, SessionKey sessionKey) {
        this.equation = equation;
        this.clientSocket = clientSocket;
        this.keys = keys;
        this.sessionKey = sessionKey;

        // Establish Sender
        sender = clientSocket.sender;
        // Establish Receiver
        receiver = clientSocket.receiver;

        createAndShowGUI();
    }


    private void createAndShowGUI() {
        JFrame frame = new JFrame("Math Equation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("Enter the solution to the equation:" + "  " + equation);

        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField solutionField = new JTextField();
        JButton submitButton = new JButton("Submit");

        panel.add(messageLabel);
        panel.add(solutionField);
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientSolution = solutionField.getText();
                sendSolution(clientSolution);
                try {
                    String encryptedDigitalCertificate = (String) receiver.readObject();
                    Message decryptedDigitalCertificate=SessionKey.decrypt(encryptedDigitalCertificate,sessionKey.getSessionKey());
                    DigitalCertificate digitalCertificate=new DigitalCertificate() ;
                    digitalCertificate.parseToModel(decryptedDigitalCertificate.getMessage());
                    Utils.storeObject(digitalCertificate);
                    frame.dispose();
                } catch (IOException | ClassNotFoundException | InvalidKeySpecException | NoSuchAlgorithmException | SignatureException | InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.add(panel);
        frame.setVisible(true);
    }

    private void sendSolution(String clientSolution) {
        byte[] bytes = JavaPGP.reverseencrypt(clientSolution.getBytes(), keys.getPrivate());
        try {
            sender.writeObject(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




}
