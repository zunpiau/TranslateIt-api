package zjp.translateit.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Donation {

    @Length(min = 32, max = 32)
    private String trade;
    @JsonDeserialize(using = Deserializer.class)
    @JsonSerialize(using = Serializer.class)
    private LocalDateTime time;
    private String name;
    private int amount;
    private String comment;

    public Donation(LocalDateTime time, String name, int amount, String comment) {
        this.time = time;
        this.name = name;
        this.amount = amount;
        this.comment = comment;
    }

    public Donation() {
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class Deserializer extends LocalDateTimeDeserializer {

        public Deserializer() {
            this(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        public Deserializer(DateTimeFormatter formatter) {
            super(formatter);
        }
    }

    public static class Serializer extends LocalDateTimeSerializer {

        public Serializer() {
            this(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        public Serializer(DateTimeFormatter formatter) {
            super(formatter);
        }
    }

}
