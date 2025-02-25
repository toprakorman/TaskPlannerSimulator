package project.Strategy;

public class BirthdayCategory implements CategoryStrategy {
    @Override
    public String getCategoryName() {
        return "Birthday";
    }
}