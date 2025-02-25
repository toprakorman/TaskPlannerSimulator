package project.message;

import java.time.LocalDate;
import java.time.Period;
import project.Task.Task;

public class NotificationMessageDecorator implements Message {

    private final Message message;
    private final Task task;

    public NotificationMessageDecorator(Message message, Task task) {
        this.message = message;
        this.task = task;
    }

    @Override
    public String getMessage(LocalDate localdate) {
        if (localdate.now().equals(task.getDeadline())) {
            return message.getMessage(localdate) + " - " + task.getName() + " Task Day!";
        } else {
            Period period = Period.between(localdate.now(), task.getDeadline());
            int yearsLeft = period.getYears();
            int monthsLeft = period.getMonths();
            int daysLeft = period.getDays();

            StringBuilder messageBuilder = new StringBuilder(message.getMessage(localdate));
            messageBuilder.append(" - ");

            /*
        if (yearsLeft > 0) {
            messageBuilder.append(yearsLeft).append(" year(s) ");
        }
        if (monthsLeft > 0) {
            messageBuilder.append(monthsLeft).append(" month(s) ");
        }
        if (daysLeft > 0 || (yearsLeft == 0 && monthsLeft == 0)) { // If there are no years or months left, always show days.
            messageBuilder.append(daysLeft).append(" day(s) ");
        }
             */
            messageBuilder.append("Time left until the ").append(task.getName()).append(" task: ");

            return messageBuilder.toString();
        }
    }
}
