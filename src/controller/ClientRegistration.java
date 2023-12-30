package controller;

import api.ClientSocket;
import api.Encryption;
import api.Operation;
import exception.CustomException;
import model.Message;
import model.RegistrationModel;

public class ClientRegistration {
    private final ClientSocket clientSocket;

    public ClientRegistration(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public RegistrationModel Registration(RegistrationModel model) throws CustomException {
        //Create Message that hold model
        Message request = new Message(model, Operation.Register);

        //Send Message and Encrypt Message using AES
        Message response = clientSocket.sendMessageToServer(request, Encryption.AES);

        //return Response Body
        return (RegistrationModel) response.getBody();
    }
}
