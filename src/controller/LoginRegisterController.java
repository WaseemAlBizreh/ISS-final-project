package controller;

import exception.CustomException;

import java.security.NoSuchAlgorithmException;

abstract public class LoginRegisterController {

    //abstract public void login(String username, String password) throws CustomException, NoSuchAlgorithmException;

    abstract public int register(String username, String password) throws CustomException, NoSuchAlgorithmException;
}
