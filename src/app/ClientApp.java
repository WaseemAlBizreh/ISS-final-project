package app;

import api.ClientSocket;
import view.ConnectView;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientSocket clientSocket = new ClientSocket();
                ConnectView clientView = new ConnectView(clientSocket);
            }
        });
    }
}
