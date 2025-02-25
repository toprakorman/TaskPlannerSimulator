package project.message;

import java.time.LocalDate;
import java.time.Period;

public class BirthdayMessageDecorator implements Message {

    private final Message message;
    private final LocalDate birthdate;

    public BirthdayMessageDecorator(Message message, LocalDate birthdate) {
        this.message = message;
        this.birthdate = birthdate;
    }

    @Override
    public String getMessage(LocalDate localdate) {
        if (localdate.now().equals(birthdate)) {
            return message.getMessage(localdate) + " - Happy Birthday!";
        } else {
            Period period = Period.between(localdate.now(), birthdate);
            int yearsLeft = period.getYears();
            int monthsLeft = period.getMonths();
            int daysLeft = period.getDays();

            StringBuilder messageBuilder = new StringBuilder(message.getMessage(localdate))
                    .append(" - ");

            /* if (yearsLeft > 0) {
                messageBuilder.append(yearsLeft).append(" year(s) ");
            }
            if (monthsLeft > 0) {
                messageBuilder.append(monthsLeft).append(" month(s) ");
            }
            if (daysLeft > 0) {
                messageBuilder.append(daysLeft).append(" day(s) ");
            }
             */
            messageBuilder.append("Time left until the Birthday Party: ");

            return messageBuilder.toString();
        }
    }
}
