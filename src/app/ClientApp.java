package app;

import api.ClientSocket;
import controller.Client_Login_registerController;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import exception.CustomException;
import model.AddData;
import model.RegistrationModel;
import repository.AddProjectsRepository;
import repository.AddmaterialmarksRepository;
import view.ConnectView;
import view.RegistrationForm;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;

public class ClientApp {


    public static void main(String[] args) throws NoSuchAlgorithmException, CustomException {
/*
      ServerRegistration d = new ServerRegistration();
        RegistrationModel r = new RegistrationModel(4,"d", "7734345", "0994554081", "455","0311","Student");

        RegistrationModel e= d.Registration(r);
        System.out.println(e.email);

*/
/*
        AddData de = new AddData(30,"ww","www");
        AddProjectsRepository v = new AddProjectsRepository();
        v.Addprojects(de);
        Server_login_registerController d = new Server_login_registerController();

*/


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientSocket clientSocket = new ClientSocket();
               ConnectView clientView = new ConnectView(clientSocket);
              //  RegistrationForm m = new RegistrationForm(clientSocket);
            }
        });
    }
}
