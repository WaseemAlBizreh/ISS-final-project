package model;

public class    LoginRegisterModel extends Model {
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
        builder.append("username:logreg: ").append(username).append(" .logreg. ");
        builder.append("password:logreg: ").append(password);
        return builder.toString();
    }

    @Override
    public void parseToModel(String message) {
        String[] parts = message.split(" .logreg. ");
        for (String part : parts) {
            String[] keyValue = part.split(":logreg: ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "username":
                        this.setUsername(value);
                        break;
                    case "password":
                        this.setPassword(value);
                        break;
                }
            }
        }
    }
}
