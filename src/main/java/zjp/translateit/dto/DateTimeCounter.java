package zjp.translateit.dto;

public class DateTimeCounter extends DateCounter {

    private final String time;

    public DateTimeCounter(String date, String time, int count) {
        super(date, count);
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
