package zjp.translateit.dto;

public class DateTimeCounter {

    private final String date;
    private final String time;
    private final int count;

    public DateTimeCounter(String date, String time, int count) {
        this.date = date;
        this.time = time;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }
}
