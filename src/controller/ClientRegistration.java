package controller;

import api.ClientSocket;
import api.Encryption;
import api.Operation;
import exception.CustomException;
import model.LoginRegisterModel;
import model.Message;
import model.Model;
import model.RegistrationModel;
import security.AES;

public class ClientRegistration {
    private final ClientSocket clientSocket;








    public ClientRegistration(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public RegistrationModel Registration(RegistrationModel model) throws CustomException {

       Message request = new Message( model , Operation.Register);

      Message response = clientSocket.sendMessageToServer(request, Encryption.None);
        RegistrationModel resp= (RegistrationModel) response.getBody();
        return resp;
     // System.out.println("Server: " + response);

    }
}
