package kibar.readingisgood.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ListOrderBetweenDateInvalidInputException extends RuntimeException {

    public ListOrderBetweenDateInvalidInputException(String message) {
        super(message);
    }

}
