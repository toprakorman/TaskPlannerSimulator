package project.Task;

import java.util.ArrayList;
import java.util.List;

public class CompositeTask implements TaskComponent {
    private String name;
    private ArrayList<Task> taskComponents = new ArrayList<>();
    

    public CompositeTask(String name) {
        this.name = name;
    }

    @Override
    public void displayTasks() {
        System.out.println("Composite Task: " + name);
        for (TaskComponent taskComponent : taskComponents) {
            taskComponent.displayTasks();
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return taskComponents;
    }

   
    public void addTask(Task task) {
        taskComponents.add(task);
    }

   
    public void removeTask(Task task) {
        taskComponents.remove(task);
    }
}
