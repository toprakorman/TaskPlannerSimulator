package project.message;

import java.time.LocalDate;

public class DateMessageDecorator implements Message {

    private final Message message;

    public DateMessageDecorator(Message message) {
        this.message = message;
    }

    @Override
    public String getMessage(LocalDate localdate) {
        return "Date: " + message.getMessage(localdate).split(",")[1].trim();
    }
}
