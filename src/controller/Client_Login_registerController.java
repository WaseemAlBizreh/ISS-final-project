package controller;

import api.ClientSocket;
import api.Encryption;
import api.Operation;
import exception.CustomException;
import model.LoginRegisterModel;
import model.Message;
import model.RegistrationModel;
import security.AES;


public class Client_Login_registerController extends LoginRegisterController {
    String d;
    private final ClientSocket clientSocket;

    public Client_Login_registerController(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public RegistrationModel login(String username, String password) throws CustomException {
        //Create Login Register Model
        LoginRegisterModel model = new LoginRegisterModel(username, password);

        //Create Message to Send Information
        Message request = new Message(model, Operation.Login);

        // Send Message to Server
        Message response = clientSocket.sendMessageToServer(request, Encryption.None);

        // Get Response Body
        RegistrationModel responseBody = (RegistrationModel) response.getBody();

        //Check if response in Success
        if (responseBody != null) {
            //Set Symmetric Key in Client is password
            ClientSocket.symmetricKey = AES.generateSecretKey(model.password);
        }

        return responseBody;
    }

    @Override
    public int register(String username, String password) throws CustomException {

        LoginRegisterModel model = new LoginRegisterModel(username, password);
        model.username = username;
        model.password = password;

        try {

            Message request = new Message(model, Operation.SignUp);
            ClientSocket.symmetricKey = AES.generateSecretKey(model.password);
            Message response = clientSocket.sendMessageToServer(request, Encryption.None);

            System.out.println("Server: " + response);
            //response.getMessage();
            return Integer.parseInt(response.getMessage());
        } catch (CustomException e) {
            System.out.println("error");
        }
        //  Message request = new Message(model, Operation.SignUp);
        // Message response = clientSocket.sendMessageToServer(request);
        //   System.out.println("Server: " + response);
        return 0;
    }

}
