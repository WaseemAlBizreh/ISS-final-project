package controller;

import model.LoginRegisterModel;
import model.RegistrationModel;
import repository.LoginRepository;
import repository.RegisterRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Server_login_registerController extends LoginRegisterController {

    RegisterRepository registerRepository = new RegisterRepository();
    LoginRepository loginRepository = new LoginRepository();




   // @Override
    public RegistrationModel login(String username, String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = password.getBytes();
        md.update(passwordBytes);
        byte[] hashedPasswordBytes = md.digest();
        String hashedPassword = Base64.getEncoder().encodeToString(hashedPasswordBytes);


        LoginRegisterModel credentials = new LoginRegisterModel(username, hashedPassword);

        RegistrationModel loginSuccessful = loginRepository.checkCredentials(credentials);
        if (loginSuccessful != null) {
            System.out.println("تم تسجيل الدخول بنجاح");
            return loginSuccessful;
        } else {
            System.out.println("فشل في تسجيل الدخول");
            return loginSuccessful;
        }

    }

    @Override
    public int register(String username, String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = password.getBytes();
        md.update(passwordBytes);
        byte[] hashedPasswordBytes = md.digest();
        String hashedPassword = Base64.getEncoder().encodeToString(hashedPasswordBytes);


        LoginRegisterModel newUser = new LoginRegisterModel(username, hashedPassword);
        int registrationSuccessful = registerRepository.insertRows(newUser);
        if (registrationSuccessful > 0 ) {
            System.out.println("تم تسجيل المستخدم بنجاح");
            return  registrationSuccessful;
        } else {
            System.out.println("فشل في تسجيل المستخدم");
        }
        return 0;
    }


}
