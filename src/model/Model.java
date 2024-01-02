package model;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

abstract public class Model implements Serializable {
    public abstract String toString();
    public abstract void parseToModel(String message) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;
}
