package model;

public class RegistrationModel extends Model {
    public int id;
    public String username;
    public String email;
    public String phoneNumber;
    public String mobileNumber;
    public String address;
    public String nationalNumber;
    public String role;

    public RegistrationModel() {
    }

    public RegistrationModel(int id, String username, String email, String phoneNumber, String mobileNumber, String address, String nationalNumber, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.nationalNumber = nationalNumber;
        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id:reg: ").append(id).append(" .reg. ");
        builder.append("username:reg: ").append(username).append(" .reg. ");
        builder.append("email:reg: ").append(email).append(" .reg. ");
        builder.append("phoneNumber:reg: ").append(phoneNumber).append(" .reg. ");
        builder.append("mobileNumber:reg: ").append(mobileNumber).append(" .reg. ");
        builder.append("address:reg: ").append(address).append(" .reg. ");
        builder.append("nationalNumber:reg: ").append(nationalNumber).append(" .reg. ");
        builder.append("role:reg: ").append(role);
        return builder.toString();
    }

    @Override
    public void parseToModel(String message) {
        String[] parts = message.split(" .reg. ");
        for (String part : parts) {
            String[] keyValue = part.split(":reg: ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "id":
                        this.id = Integer.parseInt(value);
                        break;
                    case "username":
                        this.username = value;
                        break;
                    case "email":
                        this.email = value;
                        break;
                    case "phoneNumber":
                        this.phoneNumber = value;
                        break;
                    case "mobileNumber":
                        this.mobileNumber = value;
                        break;
                    case "address":
                        this.address = value;
                        break;
                    case "nationalNumber":
                        this.nationalNumber = value;
                        break;
                    case "role":
                        this.role = value;
                        break;
                }
            }
        }
    }
}
