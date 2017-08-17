package zjp.translateit.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zjp.translateit.web.domain.Response;
import zjp.translateit.web.exception.BadRequestException;
import zjp.translateit.web.exception.InnerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public Response badRequestException(BadRequestException e) {
        return new Response(400, e.getErrorMessage());
    }

    @ExceptionHandler(InnerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response innerException(InnerException e) {
        return new Response(500, e.getErrorMessage());
    }

}
