
package view;

import api.ClientSocket;
import api.Operation;
import controller.ClientRegistration;
import exception.CustomException;
import model.Message;
import model.RegistrationModel;
import security.SessionKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.util.Objects;

public class CARegistrationForm {

    private final ClientSocket clientSocket;
    private final int userid;
    private final String username;
    private final ClientRegistration controller;
    private JFrame frame;
    private JTextField emailField;
    private JTextField mobileNumberField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField nationalNumberField;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;

    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;
    private final KeyPair keys;

    private final SessionKey sessionKey;
    public CARegistrationForm(ClientSocket clientSocket, int id, String username, KeyPair keys,SessionKey sessionKey) {
        this.clientSocket = clientSocket;
        this.sessionKey =sessionKey;
        this.username = username;
        this.controller = new ClientRegistration(clientSocket);
        this.keys = keys;
        this.userid = id;

        // Establish Sender
        sender = clientSocket.sender;
        // Establish Receiver
        receiver = clientSocket.receiver;

        createAndShowGUI();
    }


    private void createAndShowGUI() {
        frame = new JFrame("Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Registration Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField = new JTextField();
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        JLabel mobileNumberLabel = new JLabel("Mobile Number:");
        mobileNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mobileNumberField = new JTextField();
        formPanel.add(mobileNumberLabel);
        formPanel.add(mobileNumberField);

        JLabel phoneNumberLabel = new JLabel("Telephone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        phoneNumberField = new JTextField();
        formPanel.add(phoneNumberLabel);
        formPanel.add(phoneNumberField);


        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        addressField = new JTextField();
        formPanel.add(addressLabel);
        formPanel.add(addressField);

        JLabel nationalNumberLabel = new JLabel("National Number:");
        nationalNumberLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nationalNumberField = new JTextField();
        formPanel.add(nationalNumberLabel);
        formPanel.add(nationalNumberField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(roleLabel);

        ButtonGroup roleGroup = new ButtonGroup();
        studentRadioButton = new JRadioButton("Student");
        teacherRadioButton = new JRadioButton("Teacher");
        roleGroup.add(studentRadioButton);
        roleGroup.add(teacherRadioButton);

        JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(studentRadioButton);
        rolePanel.add(teacherRadioButton);
        formPanel.add(rolePanel);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.CYAN);
        sendButton.setBorder(BorderFactory.createRaisedBevelBorder());
        sendButton.setPreferredSize(new Dimension(120, 40));
        sendButton.setMargin(new Insets(10, 10, 10, 10));
        buttonPanel.add(sendButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registration();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void registration() {
        // Get TextFields Values
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String mobileNumber = mobileNumberField.getText();
        String address = addressField.getText();
        String nationalNumber = nationalNumberField.getText();
        String role = "";
        if (studentRadioButton.isSelected() && !teacherRadioButton.isSelected()) {
            role = "Student";
        } else if (teacherRadioButton.isSelected() && !studentRadioButton.isSelected()) {
            role = "Teacher";
        }
        // Check Enter Data
        if (email.isEmpty() || phoneNumber.isEmpty() || mobileNumber.isEmpty() ||
                address.isEmpty() || nationalNumber.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter all the information");
        } else {
            // Create Model that hold All Form Data
            RegistrationModel model = new RegistrationModel(userid, username, email, phoneNumber, mobileNumber, address, nationalNumber, role);

            //Do Registration Form Operation
            try {
                String userId=String.valueOf(userid);
                Message useridmessage=new Message(userId,Operation.SignUp);
                Message usernamemessage=new Message(username,Operation.SignUp);
                Message emailmessage=new Message(email,Operation.SignUp);
                Message phoneNumbermessage=new Message(phoneNumber,Operation.SignUp);
                Message mobileNumbermessage=new Message(mobileNumber,Operation.SignUp);
                Message addressmessage=new Message(address,Operation.SignUp);
                Message nationalNumbermessage=new Message(nationalNumber,Operation.SignUp);
                Message rolemessage=new Message(role,Operation.SignUp);
                sender.writeObject(SessionKey.encrypt(useridmessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(usernamemessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(emailmessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(phoneNumbermessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(mobileNumbermessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(addressmessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(nationalNumbermessage,sessionKey.getSessionKey()));
                sender.writeObject(SessionKey.encrypt(rolemessage,sessionKey.getSessionKey()));
                //RegistrationModel response = controller.Registration(model);
                Message equation=SessionKey.decrypt((String) receiver.readObject(),sessionKey.getSessionKey());
                if (equation.getMessage() == null) {
                    JOptionPane.showMessageDialog(frame, "Please enter all the information",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException();
                } else {
                    EquationUI equationUI=new EquationUI(equation.getMessage(),clientSocket,keys,sessionKey);

                }
            } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException ex) {
                ex.printStackTrace();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}