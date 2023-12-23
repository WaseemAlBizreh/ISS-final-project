package model;

public class LoginRegisterModel extends Model{
    public String username;
    public String password;

    public LoginRegisterModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
