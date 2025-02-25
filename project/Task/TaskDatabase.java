package project.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import project.Strategy.BirthdayCategory;
import project.Strategy.CategoryStrategy;
import project.DatabaseConnection;
import project.Strategy.HolidayCategory;
import project.Strategy.HomeCategory;
import project.Strategy.WorkCategory;

public class TaskDatabase {

    private Connection connection;

    public TaskDatabase() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                description TEXT,
                category VARCHAR(50),
                deadline DATE
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        }
    }

    public void addTask(Task task) throws SQLException {
        String query = "INSERT INTO tasks (name, description, category, deadline) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getCategoryName());
            stmt.setDate(4, Date.valueOf(task.getDeadline()));
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void editTask(Task task) throws SQLException {
        String query = "UPDATE tasks SET name = ?, description = ?, category = ?, deadline = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getCategoryName());
            stmt.setDate(4, Date.valueOf(task.getDeadline()));
            stmt.setInt(5, task.getId());
            stmt.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                CategoryStrategy categoryStrategy = createCategoryStrategy(rs.getString("category"));
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        categoryStrategy,
                        rs.getDate("deadline").toLocalDate()
                );
                tasks.add(task);
            }
        }
        return tasks;
    }

    private CategoryStrategy createCategoryStrategy(String categoryName) {
        return switch (categoryName.toLowerCase()) {
            case "work" ->
                new WorkCategory();
            case "home" ->
                new HomeCategory();
            case "holiday" ->
                new HolidayCategory();
            case "birthday" ->
                new BirthdayCategory();
            default ->
                throw new IllegalArgumentException("Unknown category: " + categoryName);
        };
    }

    public Task getTaskById(int taskId) throws SQLException {
        String query = "SELECT * FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String categoryName = rs.getString("category");
                CategoryStrategy categoryStrategy = createCategoryStrategy(categoryName);

                return new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        categoryStrategy,
                        rs.getDate("deadline").toLocalDate()
                );
            } else {
                return null;
            }
        }
    }

}
