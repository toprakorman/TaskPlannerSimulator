package project.message;

import java.time.LocalDate;

public class BasicMessage implements Message {

    @Override
    public String getMessage(LocalDate localdate) {
        return localdate.getDayOfWeek() + ", " + localdate.now();
    }
}
