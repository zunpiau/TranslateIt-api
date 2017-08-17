package zjp.translateit.web.domain;

public class Response {

    private int statusCode;
    private String message;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static Response getResponseOK() {
        return new Response(200, "");
    }

}
