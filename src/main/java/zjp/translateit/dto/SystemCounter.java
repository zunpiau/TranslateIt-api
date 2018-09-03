package zjp.translateit.dto;

import java.util.List;

public class SystemCounter {

    private final long user;
    private final long wordbook;
    private final List<DateCounter> dateCounters;
    private final List<DateTimeCounter> dateTimeCounters;

    public SystemCounter(long user,
            long wordbook,
            List<DateCounter> dateCounters,
            List<DateTimeCounter> dateTimeCounters) {
        this.user = user;
        this.wordbook = wordbook;
        this.dateCounters = dateCounters;
        this.dateTimeCounters = dateTimeCounters;
    }

    public long getUser() {
        return user;
    }

    public List<DateCounter> getDateCounters() {
        return dateCounters;
    }

    public List<DateTimeCounter> getDateTimeCounters() {
        return dateTimeCounters;
    }

    public long getWordbook() {
        return wordbook;
    }
}
