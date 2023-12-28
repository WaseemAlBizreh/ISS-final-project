package repository;

import model.Model;
//import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract public class Repository {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/iss-project";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public String tableName;

    // Method to get a database connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    abstract public boolean insertRow(Model newData);

    abstract public boolean updateRow(int id, Model newData);

    public boolean deleteRow(int id) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
