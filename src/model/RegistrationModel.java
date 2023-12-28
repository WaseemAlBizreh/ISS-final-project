package model;

import api.Operation;

public class RegistrationModel extends Model{
    public int id;
    public String username;
    public String email;
    public String phoneNumber;
    public String mobileNumber;
    public String address;
    public String nationalNumber;
    public String role ;

    public RegistrationModel(){};

    public RegistrationModel( int id, String email, String phoneNumber, String mobileNumber, String address, String nationalNumber,String role ) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.nationalNumber = nationalNumber;
        this.role = role;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void parseToModel(String message) {

    }
}
