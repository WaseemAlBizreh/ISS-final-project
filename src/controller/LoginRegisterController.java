package controller;

import exception.CustomException;
import model.RegistrationModel;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

abstract public class LoginRegisterController {

    abstract public RegistrationModel login(String username, String password) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, SignatureException;

    abstract public int register(String username, String password) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}
