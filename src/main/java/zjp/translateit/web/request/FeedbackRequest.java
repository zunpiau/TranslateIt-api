package zjp.translateit.web.request;

import javax.validation.constraints.NotNull;

public class FeedbackRequest {

    @NotNull
    private String content;
    @NotNull
    private String contact;

    public FeedbackRequest() {
    }

    public FeedbackRequest(String content, String contact) {
        this.content = content;
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public String getContact() {
        return contact;
    }
}
