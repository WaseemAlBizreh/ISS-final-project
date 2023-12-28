package view;
import api.ClientSocket;
import controller.ClientAddProjectOrMarks;
import controller.ClientRegistration;
import exception.CustomException;
import model.AddData;
import model.RegistrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectsView {

    private final ClientSocket clientSocket;
    RegistrationModel mod;
    public ProjectsView(ClientSocket clientSocket , RegistrationModel mod) {
        this.clientSocket = clientSocket;
        this.mod= mod;
        createAndShowGUI();


    }

    private  void createAndShowGUI() {
        JFrame frame = new JFrame("Projects");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // تعيين حجم النافذة بشكل مباشر
        frame.setSize(600, 370); // تم تعيين أبعاد أكبر هنا
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Projects Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField descriptionField = new JTextField();
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);

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
                String description = descriptionField.getText();

                if (description.isEmpty()  )
                {
                    JOptionPane.showMessageDialog(frame, "Please enter the description");

                }
                else
                {
                AddData data =new AddData(mod.id,description);


                ClientAddProjectOrMarks v = new ClientAddProjectOrMarks(clientSocket);
                try {
                    int r =  v.addProject(data);
                    System.out.println(r);
                    JOptionPane.showMessageDialog(frame, "The description has been successfully added");
                } catch (CustomException ex) {
                    ex.printStackTrace();
                }




             //   JOptionPane.showMessageDialog(frame, "Description: " + description, "Project Details", JOptionPane.INFORMATION_MESSAGE);
            }}
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}


