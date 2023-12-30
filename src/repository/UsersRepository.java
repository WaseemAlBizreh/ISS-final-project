package repository;

import model.LoginRegisterModel;
import model.Model;
import model.RegistrationModel;

import java.sql.*;

public class UsersRepository extends Repository {

    public UsersRepository() {
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
                String nationalNumber = resultSet.getString("encryption_key");
                String role = resultSet.getString("role");
                return new RegistrationModel(id, username, email, phoneNumber, mobileNumber, address, nationalNumber, role);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int insertRows(LoginRegisterModel newData) {
        String sql = String.format("INSERT INTO %s (username, password) VALUES (?, ?)", tableName);

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Check if User already Exist
            RegistrationModel user = checkCredentials(newData);
            if (user != null) {
                // User Already Exist
                return 0;
            } else {
                // Execute SignUp Operation in DB
                preparedStatement.setString(1, newData.username);
                preparedStatement.setString(2, newData.password);

                //Execute Query
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Get Result Set
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    // Check resultSet
                    if (resultSet.first()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new SQLException("No ID obtained.");
                    }
                } else {
                    throw new SQLException("No rows affected.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public RegistrationModel updateRegistration(RegistrationModel newData) {
        String sql = String.format("UPDATE %s SET email = ?, phone_number = ?, mobile_number = ?, address = ?, encryption_key = ?, role = ?, created_at = CURRENT_TIMESTAMP,updated_at = CURRENT_TIMESTAMP WHERE id = ?", tableName);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newData.email);
            preparedStatement.setString(2, newData.phoneNumber);
            preparedStatement.setString(3, newData.mobileNumber);
            preparedStatement.setString(4, newData.address);
            preparedStatement.setString(5, newData.nationalNumber);
            preparedStatement.setString(6, newData.role);
            preparedStatement.setInt(7, newData.id);

            //Execute Query
            int rowsAffected = preparedStatement.executeUpdate();
            // Check rowsAffected
            if (rowsAffected > 0) {
                return newData;
            } else {
                throw new SQLException("No rows affected.");
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
