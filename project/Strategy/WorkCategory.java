package project.Strategy;

public class WorkCategory implements CategoryStrategy {
    @Override
    public String getCategoryName() {
        return "Work";
    }
}