package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private DatabaseConnection databaseConnection;

    public Project() {
        databaseConnection = new DatabaseConnection();
    }

    public List<String[]> getProjectList() {
        List<String[]> projects = new ArrayList<>();
        String query = "SELECT projectid, projectname FROM \"IMPACT Club\".project";

        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String projectId = String.valueOf(resultSet.getInt("projectid"));
                String projectName = resultSet.getString("projectname");
                projects.add(new String[]{projectId, projectName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
