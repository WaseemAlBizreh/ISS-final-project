
package view;

import api.ClientSocket;
import controller.ClientRegistration;
import exception.CustomException;
import model.RegistrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.security.KeyPair;
        import java.util.Objects;

public class RegistrationForm {

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

    public RegistrationForm(ClientSocket clientSocket, int id, String username) {
    private final KeyPair keys;
int userid;


    public RegistrationForm(ClientSocket clientSocket , int id,KeyPair keys ) {
        this.clientSocket = clientSocket;
        this.userid = id;
        this.username = username;
        this.controller = new ClientRegistration(clientSocket);
        this.keys = keys;
        this.userid= id;
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
                RegistrationModel response = controller.Registration(model);
                if (response == null) {
                    JOptionPane.showMessageDialog(frame, "Please enter all the information",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else if (Objects.equals(response.role, "Student")) {
                    ProjectsView pro = new ProjectsView(clientSocket, response);
                    frame.dispose();
                } else {
                    MarksView mar = new MarksView(clientSocket, response);
                    frame.dispose();
                }
            } catch (CustomException ex) {
                ex.printStackTrace();
            }
        }
    }
}