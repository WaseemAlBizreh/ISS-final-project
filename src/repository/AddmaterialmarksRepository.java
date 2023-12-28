package repository;

import model.AddData;
import model.Model;

import java.sql.*;

public class AddmaterialmarksRepository extends Repository{

    public AddmaterialmarksRepository() {
        tableName = "material_marks";
    }



    public int insertMaterialData(AddData newMaterialData) {
        String sql = String.format("INSERT INTO material_marks (material_name, content, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?)");

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {



                // المستخدم موجود، يتم إدراج البيانات في جدول المواد
                preparedStatement.setString(1, newMaterialData.name);
                preparedStatement.setString(2, newMaterialData.content);
                preparedStatement.setInt(3, newMaterialData.id);

                // تحديث الأوقات المرتبطة بالإنشاء والتحديث
                java.util.Date currentDate = new java.util.Date();
                java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());
                preparedStatement.setTimestamp(4, timestamp);
                preparedStatement.setTimestamp(5, timestamp);

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

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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
