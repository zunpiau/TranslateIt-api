package zjp.translateit.dto;

public class DateCounter {

    private final String date;
    private final int count;

    public DateCounter(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }
}
