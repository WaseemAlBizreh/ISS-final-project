package app;

import api.ClientSocket;
import security.GenerateKeys;
import view.ConnectView;

import javax.swing.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ClientApp {
    public static void main(String[] args) {
        Utils utils=new Utils();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                KeyPair keys=   utils.checkpgp();

                ClientSocket clientSocket = new ClientSocket();
                ConnectView clientView = new ConnectView(clientSocket,keys);
            }
        });
    }
}
