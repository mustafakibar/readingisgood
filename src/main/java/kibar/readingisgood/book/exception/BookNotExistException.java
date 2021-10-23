package kibar.readingisgood.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class BookNotExistException extends RuntimeException {

    public BookNotExistException(String message) {
        super(message);
    }

}
