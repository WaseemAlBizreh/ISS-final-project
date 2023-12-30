package controller;

import model.RegistrationModel;
import repository.UsersRepository;

public class ServerRegistration {

    UsersRepository usersRepository = new UsersRepository();

    // دالة استكمال معلومات المستخدم
    public RegistrationModel Registration(RegistrationModel model) {

        RegistrationModel registrationSuccessful = usersRepository.updateRegistration(model);
        if (registrationSuccessful != null) {
            System.out.println("Registration Form Operation Success");
            return registrationSuccessful;
        } else {
            System.out.println("Registration Form Operation Fail");
            return null;
        }
    }
}
