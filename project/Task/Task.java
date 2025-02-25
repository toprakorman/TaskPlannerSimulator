package project.Task;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import project.Strategy.CategoryStrategy;

public class Task implements TaskComponent {

    private int id;
    private String name;
    private String description;
    private CategoryStrategy category;
    public LocalDate deadline;

    public Task(int id, String name, String description, CategoryStrategy category, LocalDate deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.deadline = deadline;
    }

    public String getCategoryName() {
        return category.getCategoryName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryStrategy getCategory() {
        return category;
    }

    public void setCategory(CategoryStrategy category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public void displayTasks() {
        System.out.println("Task ID: " + id + ", Name: " + name + ", Category: " + category.getCategoryName() + ", Deadline: " + deadline);
    }

    @Override
    public List<Task> getTasks() {
        return Collections.singletonList(this);
    }
}
