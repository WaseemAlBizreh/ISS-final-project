package repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.LoginRegisterModel;
import model.Model;
import model.RegistrationModel;

public class LoginRepository extends Repository {

    public LoginRepository() {
        tableName = "users";
    }
/*
    public boolean checkCredentials(LoginRegisterModel credentials) {
        String sql = String.format("SELECT * FROM %s WHERE username = ? AND password = ?", tableName);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, credentials.username);
            preparedStatement.setString(2, credentials.password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
*/
public RegistrationModel checkCredentials(LoginRegisterModel credentials) {
    String sql = String.format("SELECT * FROM %s WHERE username = ? AND password = ?", tableName);
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, credentials.username);
        preparedStatement.setString(2, credentials.password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String phoneNumber = resultSet.getString("phone_number");
            String mobileNumber = resultSet.getString("mobile_number");
            String address = resultSet.getString("address");
           // String nationalNumber = resultSet.getString("nationalNumber");
            String role = resultSet.getString("role");


            RegistrationModel ss = new RegistrationModel();
            ss.id=id;
            ss.role=role;
            return ss;
        } else {
            return null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}





    @Override
    public boolean insertRow(Model newData) {
        return false;
    }

    @Override
    public boolean updateRow(int id, Model newData) {
        return false;
    }
}
