package controller;

import exception.CustomException;
import model.RegistrationModel;

import java.security.NoSuchAlgorithmException;

abstract public class LoginRegisterController {

    abstract public RegistrationModel login(String username, String password) throws CustomException, NoSuchAlgorithmException;

    abstract public int register(String username, String password) throws CustomException, NoSuchAlgorithmException;
}
