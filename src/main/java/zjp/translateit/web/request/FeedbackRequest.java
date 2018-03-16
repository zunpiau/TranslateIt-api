package zjp.translateit.web.request;

import javax.validation.constraints.NotNull;

public class FeedbackRequest {

    @NotNull
    private String content;
    @NotNull
    private String contact;
    private String version;

    public FeedbackRequest() {
    }

    public FeedbackRequest(String content, String contact, String version) {
        this.content = content;
        this.contact = contact;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getContent() {
        return content;
    }

    public String getContact() {
        return contact;
    }
}
