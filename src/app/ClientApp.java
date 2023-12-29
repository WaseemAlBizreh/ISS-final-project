package app;

import api.ClientSocket;
import view.ConnectView;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        Utils utils=new Utils();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                utils.checkPgp();
                ClientSocket clientSocket = new ClientSocket();
                ConnectView clientView = new ConnectView(clientSocket);
            }
        });
    }
}
