package project.Strategy;

public class HomeCategory implements CategoryStrategy {
    @Override
    public String getCategoryName() {
        return "Home";
    }
}