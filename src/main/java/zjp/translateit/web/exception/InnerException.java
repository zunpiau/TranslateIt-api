package zjp.translateit.web.exception;

public class InnerException extends RuntimeException {

    private String errorMessage;

    public InnerException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
