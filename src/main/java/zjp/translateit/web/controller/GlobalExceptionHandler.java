package zjp.translateit.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zjp.translateit.web.exception.EmailSendException;
import zjp.translateit.web.response.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailSendException.class)
    public Response handelEmailSendException() {
        return new Response(Response.ResponseCode.EMAIL_SEND_FAILED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public void httpMediaTypeNotSupported() {

    }

    @ExceptionHandler(Exception.class)
    public Response badRequestException() {
        return new Response(Response.ResponseCode.INNER_EXCEPTION);
    }

}
