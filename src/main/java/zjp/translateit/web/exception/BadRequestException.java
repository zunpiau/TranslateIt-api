package zjp.translateit.web.exception;

public class BadRequestException extends RuntimeException {

    public static final String MESSAGE_USER_DELETED = "该用户已被删除";
    public static final String MESSAGE_USER_REGISTERED = "该用户名已被注册";
    private String errorMessage;

    public BadRequestException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
