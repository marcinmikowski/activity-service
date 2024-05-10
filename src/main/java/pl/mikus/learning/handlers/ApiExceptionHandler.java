package pl.mikus.learning.handlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.TimeoutException;

@RestControllerAdvice
@Log4j2
public class ApiExceptionHandler {


    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public String handleTimeoutException() {
       log.info("Received timout exception");
       return "Some timeout has occured...";
    }
}
