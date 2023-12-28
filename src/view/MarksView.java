package view;

import api.ClientSocket;
import controller.ClientAddProjectOrMarks;
import exception.CustomException;
import model.AddData;
import model.RegistrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarksView {

    private final ClientSocket clientSocket;
    RegistrationModel mod;
    public MarksView(ClientSocket clientSocket , RegistrationModel mod) {
        this.clientSocket = clientSocket;
        this.mod= mod;
        createAndShowGUI();


    }



    private  void createAndShowGUI() {
        JFrame frame = new JFrame("marks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add Marks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel materialNameLabel = new JLabel("Material Name:");
        materialNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField materialNameField = new JTextField();
        formPanel.add(materialNameLabel);
        formPanel.add(materialNameField);

        JLabel descriptionLabel = new JLabel("Marks:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField descriptionField = new JTextField();
        descriptionField.setPreferredSize(new Dimension(200, 40)); // زيادة حجم حقل الإدخال هنا
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
                String materialName = materialNameField.getText();

                if (description.isEmpty() || materialName.isEmpty() )
                {
                    JOptionPane.showMessageDialog(frame, "Please enter all the information");

                }
                else
                {
                AddData data =new AddData(mod.id,materialName,description);


                ClientAddProjectOrMarks v = new ClientAddProjectOrMarks(clientSocket);
                try {
                    int r =  v.addMaterialMarks(data);
                    System.out.println(r);
                    JOptionPane.showMessageDialog(frame, "The marks have been successfully added");

                } catch (CustomException ex) {
                    ex.printStackTrace();
                }




                descriptionField.setCaretPosition(0);




            }}
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
