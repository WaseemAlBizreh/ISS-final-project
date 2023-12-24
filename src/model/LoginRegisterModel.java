package model;

public class LoginRegisterModel extends Model {
    public String username;
    public String password;

    public LoginRegisterModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRegisterModel() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("username: ").append(username).append(" . ");
        builder.append("password: ").append(password);
        return builder.toString();
    }

    @Override
    public Model parseToModel(String message) {
        LoginRegisterModel model = new LoginRegisterModel();
        String[] parts = message.split(" . ");
        for (String part : parts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "username":
                        model.setUsername(value);
                        break;
                    case "password":
                        model.setPassword(value);
                        break;
                }
            }
        }
        return model;
    }
}
