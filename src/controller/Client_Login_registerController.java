package controller;

import api.ClientSocket;
import api.Operation;
import exception.CustomException;
import model.LoginRegisterModel;
import model.*;

import javax.swing.*;


public class Client_Login_registerController extends LoginRegisterController {
String d;
    private final ClientSocket clientSocket;

    public Client_Login_registerController(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;

    }

    //@Override
    public RegistrationModel login(String username, String password) throws CustomException {

        LoginRegisterModel  model = new LoginRegisterModel(username,password );

      Message request = new Message( model ,Operation.Login);

     // Message response = clientSocket.sendMessageToServer(request);

        Message response = clientSocket.sendMessageToServer(request);
        RegistrationModel resp= (RegistrationModel) response.getBody();
        return resp;
     // System.out.println("Server: " + response);

    }

    @Override
    public int register(String username, String password) throws CustomException {

        LoginRegisterModel  model = new LoginRegisterModel(username,password );
             model.username= username;
             model.password= password;

        try {

            Message request = new Message(model, Operation.SignUp);
            Message response = clientSocket.sendMessageToServer(request);
            System.out.println("Server: " + response);
            //response.getMessage();
            int id = Integer.parseInt(response.getMessage());
            return id;
        } catch (CustomException e) {
            System.out.println("error");
        }
      //  Message request = new Message(model, Operation.SignUp);
       // Message response = clientSocket.sendMessageToServer(request);
     //   System.out.println("Server: " + response);
return 0;
    }

}
