package project.message;

import java.time.LocalDate;

public class DayMessageDecorator implements Message {

    private final Message message;

    public DayMessageDecorator(Message message) {
        this.message = message;
    }

    @Override
    public String getMessage(LocalDate localdate) {
        return "Day: " + message.getMessage(localdate).split(",")[0];
    }
}
