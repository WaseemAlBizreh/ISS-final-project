package repository;

import java.sql.*;

import model.LoginRegisterModel;
import model.Model;
import model.RegistrationModel;

public class RegisterRepository extends Repository {

    public RegisterRepository() {
        tableName = "users";
    }
    /*
    public boolean insertRows(LoginRegisterModel newData) {

        String sql = String.format("INSERT INTO %s (username, password) VALUES (?, ?)", tableName);

        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newData.username);
            preparedStatement.setString(2, newData.password);
            System.out.println();
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 */
    public int insertRows(LoginRegisterModel newData) {
        String sql = String.format("INSERT INTO %s (username, password) VALUES (?, ?)", tableName);

        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // تحقق من وجود المستخدم
            String checkSql = String.format("SELECT * FROM %s WHERE username = ? AND password = ?", tableName);
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, newData.username);
            checkStatement.setString(2, newData.password);
            ResultSet checkResult = checkStatement.executeQuery();
            if (checkResult.next()) {
                // المستخدم موجود
                return 0;
            } else {
                // المستخدم غير موجود
                preparedStatement.setString(1, newData.username);
                preparedStatement.setString(2, newData.password);
                System.out.println();
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        return id;
                    } else {
                        throw new SQLException("No ID obtained.");
                    }
                } else {
                    throw new SQLException("No rows affected.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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
            int rowsAffected = preparedStatement.executeUpdate();
          //  return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();

        }

        RegistrationModel t = new RegistrationModel();
        t.id=newData.id;
        t.role=newData.role;
        return newData ;
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