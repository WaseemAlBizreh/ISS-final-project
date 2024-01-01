package controller;

import model.LoginRegisterModel;
import model.RegistrationModel;
import repository.UsersRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Server_login_registerController extends LoginRegisterController {

    UsersRepository usersRepository = new UsersRepository();

    @Override
    public RegistrationModel login(String username, String password) throws NoSuchAlgorithmException {
        // Encode user Password
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = password.getBytes();
        md.update(passwordBytes);
        byte[] hashedPasswordBytes = md.digest();
        String hashedPassword = Base64.getEncoder().encodeToString(hashedPasswordBytes);

        // Create Model with hashPassword
        LoginRegisterModel credentials = new LoginRegisterModel(username, hashedPassword);

        //Check if user exist already or not
        RegistrationModel loginSuccessful = usersRepository.checkCredentials(credentials);
        if (loginSuccessful != null) {
            System.out.println("success login");
            return loginSuccessful;
        } else {
            System.out.println("fail login");
            return null;
        }
    }

    @Override
    public int register(String username, String password) throws NoSuchAlgorithmException {
        // Encode user Password
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = password.getBytes();
        md.update(passwordBytes);
        byte[] hashedPasswordBytes = md.digest();
        String hashedPassword = Base64.getEncoder().encodeToString(hashedPasswordBytes);

        // Create Model with hashPassword
        LoginRegisterModel newUser = new LoginRegisterModel(username, hashedPassword);

        // Insert User in DB
        int registrationSuccessful = usersRepository.insertRows(newUser);
        if (registrationSuccessful > 0) {
            System.out.println("success signUp");
            return registrationSuccessful;
        } else {
            System.out.println("fail signUp");
            return 0;
        }
    }
}
