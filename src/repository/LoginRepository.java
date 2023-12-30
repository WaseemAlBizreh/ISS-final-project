package repository;

import model.LoginRegisterModel;
import model.Model;
import model.RegistrationModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepository extends Repository {

    public LoginRepository() {
        tableName = "users";
    }

    public RegistrationModel checkCredentials(LoginRegisterModel credentials) {
        String sql = String.format("SELECT * FROM %s WHERE username = ? AND password = ?", tableName);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set The credentials in SQL Query
            preparedStatement.setString(1, credentials.username);
            preparedStatement.setString(2, credentials.password);
            //execute SQL Query
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String phoneNumber = resultSet.getString("phone_number");
                String mobileNumber = resultSet.getString("mobile_number");
                String address = resultSet.getString("address");
                // String nationalNumber = resultSet.getString("nationalNumber");
                String role = resultSet.getString("role");
                return new RegistrationModel(id, username, email, phoneNumber, mobileNumber, address, null, role);
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
