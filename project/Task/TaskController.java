package project.Task;

import javax.swing.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import project.Strategy.BirthdayCategory;
import project.Strategy.CategoryStrategy;
import project.Strategy.HolidayCategory;
import project.Strategy.HomeCategory;
import project.ObserverPattern.Subject;
import project.Strategy.WorkCategory;
import project.mainView;

public class TaskController extends Subject {

    private LocalDate localdate;

    public void setLocaldate(LocalDate localdate) {
        this.localdate = localdate;
    }
    private TaskDatabase taskDatabase;
    private mainView mainView;

    static HolidayTasks holidaytasks;
    static BirthdayTasks birthdaytasks;
    static WorkTasks worktasks;
    static HomeTasks hometasks;

    public TaskController(TaskDatabase taskDatabase, mainView mainView) {
        setLocaldate(LocalDate.now());
        this.taskDatabase = taskDatabase;
        this.mainView = mainView;
        holidaytasks = new HolidayTasks();
        birthdaytasks = new BirthdayTasks();
        worktasks = new WorkTasks();
        hometasks = new HomeTasks();
        DistributionofTasksTable();
        timer(this, mainView);

        mainView.getAddButton().addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter task name:");
            String description = JOptionPane.showInputDialog("Enter task description:");
            String category = JOptionPane.showInputDialog("Enter task category (work, home, holiday, birthday):");
            if (!isValidCategory(category)) {
                JOptionPane.showMessageDialog(mainView, "Invalid category! Please enter one of the following categories: work, home, holiday, birthday.");
                return;
            }
            String deadlineStr = JOptionPane.showInputDialog("Enter task deadline (YYYY-MM-DD):");
            LocalDate deadline = LocalDate.parse(deadlineStr);

            Task newTask = new Task(0, name, description, createCategoryStrategy(category), deadline);
            if (category.equalsIgnoreCase("work")) {
                worktasks.addTask(newTask);
            } else if (category.equalsIgnoreCase("home")) {
                hometasks.addTask(newTask);
            } else if (category.equalsIgnoreCase("birthday")) {
                birthdaytasks.addTask(newTask);
            } else if (category.equalsIgnoreCase("holiday")) {
                holidaytasks.addTask(newTask);
            }

            try {
                taskDatabase.addTask(newTask);
                populateTaskTable();
                notifyObservers("Task added: " + name);
                JOptionPane.showMessageDialog(mainView, "Task added successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainView, "Error adding task.");
            }
            DistributionofTasksTable();

        });

        mainView.getDeleteButton().addActionListener(e -> {
            String taskIdStr = JOptionPane.showInputDialog("Enter the task ID to delete:");
            try {
                int taskId = Integer.parseInt(taskIdStr);
                if (taskId <= 0) {
                    JOptionPane.showMessageDialog(mainView, "Please enter a valid task ID.");
                    return;
                }
                Task taskToDelete = taskDatabase.getTaskById(taskId);
                if (taskToDelete != null) {
                    taskDatabase.deleteTask(taskId);
                    populateTaskTable();
                    notifyObservers("Task deleted: " + taskToDelete.getName());
                    JOptionPane.showMessageDialog(mainView, "Task deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(mainView, "Task not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainView, "Invalid task ID. Please enter a valid number.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainView, "Error deleting task.");
            }
        });

        mainView.getEditButton().addActionListener(e -> {
            String taskIdStr = JOptionPane.showInputDialog("Enter the task ID to edit:");
            try {
                int taskId = Integer.parseInt(taskIdStr);
                if (taskId <= 0) {
                    JOptionPane.showMessageDialog(mainView, "Please enter a valid task ID.");
                    return;
                }
                Task taskToEdit = taskDatabase.getTaskById(taskId);
                if (taskToEdit == null) {
                    JOptionPane.showMessageDialog(mainView, "Task not found.");
                    return;
                }

                String name = JOptionPane.showInputDialog("Edit task name:", taskToEdit.getName());
                String description = JOptionPane.showInputDialog("Edit task description:", taskToEdit.getDescription());
                String category = JOptionPane.showInputDialog("Edit task category (work, home, holiday and birthday):", taskToEdit.getCategoryName());
                if (!isValidCategory(category)) {
                    JOptionPane.showMessageDialog(mainView, "Invalid category! Please enter one of the following categories: work, home, holiday and birthday.");
                    return;
                }
                String deadlineStr = JOptionPane.showInputDialog("Edit task deadline (YYYY-MM-DD):", taskToEdit.getDeadline().toString());
                LocalDate deadline = LocalDate.parse(deadlineStr);

                Task updatedTask = new Task(taskId, name, description, createCategoryStrategy(category), deadline);

                taskDatabase.editTask(updatedTask);
                populateTaskTable();
                notifyObservers("Task updated: " + name);
                JOptionPane.showMessageDialog(mainView, "Task updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainView, "Invalid task ID. Please enter a valid number.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainView, "Error updating task.");
            }
        });

        populateTaskTable();

    }

    private void populateTaskTable() {
        try {
            List<Task> tasks = taskDatabase.getAllTasks();
            // mainView.updateTaskNotifications(tasks);
            //mainView.updateClosestTask(tasks);

            String[][] data = new String[tasks.size()][5];
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                data[i][0] = String.valueOf(task.getId());
                data[i][1] = task.getName();
                data[i][2] = task.getDescription();
                data[i][3] = task.getCategoryName();
                data[i][4] = task.getDeadline().toString();
            }
            mainView.getTable().setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Task ID", "Task Name", "Description", "Category", "Deadline"}));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DistributionofTasksTable() {
        System.out.println("Work tasks: " + worktasks.getTasks().size());
        System.out.println("Home tasks: " + hometasks.getTasks().size());
        System.out.println("Holiday tasks: " + holidaytasks.getTasks().size());
        System.out.println("Birthday tasks: " + birthdaytasks.getTasks().size());

        int total = this.birthdaytasks.getTasks().size()
                + this.hometasks.getTasks().size()
                + this.holidaytasks.getTasks().size()
                + this.worktasks.getTasks().size();

        if (total == 0) {
            mainView.getDot().setText("<html>No new tasks</html>");
            return;
        }

        String work = this.worktasks.getTasks().size() + "/" + total;
        String holiday = this.holidaytasks.getTasks().size() + "/" + total;
        String home = this.hometasks.getTasks().size() + "/" + total;
        String birthday = this.birthdaytasks.getTasks().size() + "/" + total;

        SwingUtilities.invokeLater(() -> {
            mainView.getDot().setText("<html>"
                    + "Work: " + work + "<br>"
                    + "Holiday: " + holiday + "<br>"
                    + "Home: " + home + "<br>"
                    + "Birthday: " + birthday + "</html>");
        });
    }

    public LocalDate getLocaldate() {
        return localdate;
    }

    private CategoryStrategy createCategoryStrategy(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "work":
                return new WorkCategory();
            case "home":
                return new HomeCategory();
            case "holiday":
                return new HolidayCategory();
            case "birthday":
                return new BirthdayCategory();
            default:
                throw new IllegalArgumentException("Unknown category: " + categoryName);
        }
    }

    private boolean isValidCategory(String category) {
        return category.equalsIgnoreCase("work") || category.equalsIgnoreCase("home") || category.equalsIgnoreCase("holiday") || category.equalsIgnoreCase("birthday");
    }

    private void timer(TaskController controller, mainView mainveiw) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {

            controller.setLocaldate(controller.getLocaldate().plusDays(1));
            String dayOfWeek = controller.getLocaldate().getDayOfWeek().toString();
            Date date = Date.from(getLocaldate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy ");
            String formattedDate = formatter.format(date);
            mainveiw.setDate(formattedDate);
            mainveiw.setDay(dayOfWeek);
            try {
                List<Task> tasks = taskDatabase.getAllTasks();
                mainveiw.updateTaskNotifications(tasks, controller.getLocaldate());
                mainveiw.updateClosestTask(tasks, controller.getLocaldate());

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }, 0, 1, TimeUnit.SECONDS);

    }
}
