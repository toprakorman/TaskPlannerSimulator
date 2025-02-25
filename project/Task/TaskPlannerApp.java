package project.Task;

import project.mainView;

public class TaskPlannerApp {

    public static void main(String[] args) {
        try {
            TaskDatabase taskDatabase = new TaskDatabase();
            mainView mainView = new mainView();
            TaskController controller = new TaskController(taskDatabase, mainView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
