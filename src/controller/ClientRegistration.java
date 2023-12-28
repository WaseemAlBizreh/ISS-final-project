package controller;

import api.ClientSocket;
import api.Operation;
import exception.CustomException;
import model.LoginRegisterModel;
import model.Message;
import model.Model;
import model.RegistrationModel;

public class ClientRegistration {
    private final ClientSocket clientSocket;








    public ClientRegistration(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public RegistrationModel Registration(RegistrationModel model) throws CustomException {

       Message request = new Message( model , Operation.Register);

      Message response = clientSocket.sendMessageToServer(request);
        RegistrationModel resp= (RegistrationModel) response.getBody();
        return resp;
     // System.out.println("Server: " + response);

    }
}
