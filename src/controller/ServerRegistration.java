package controller;

import exception.CustomException;
import model.LoginRegisterModel;
import model.RegistrationModel;
import repository.RegisterRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ServerRegistration {

    RegisterRepository registerRepository = new RegisterRepository();

// دالة استكمال معلومات المستخدم
    public RegistrationModel Registration(RegistrationModel model) throws CustomException {

        RegistrationModel registrationSuccessful = registerRepository.updateRegistration(model);
        if (registrationSuccessful != null) {
            System.out.println("تم استكمال معلومات المستخدم بنجاح");
        } else {
            System.out.println("فشل في استكمال معلومات المستخدم");
        }
        return registrationSuccessful;
    }
}
