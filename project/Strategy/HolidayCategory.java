package project.Strategy;

public class HolidayCategory implements CategoryStrategy {
    @Override
    public String getCategoryName() {
        return "Holiday";
    }
}