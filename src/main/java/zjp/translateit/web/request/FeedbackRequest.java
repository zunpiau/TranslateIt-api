package zjp.translateit.web.request;

public class FeedbackRequest {

    private String content;
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
