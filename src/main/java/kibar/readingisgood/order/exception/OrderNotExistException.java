package kibar.readingisgood.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class OrderNotExistException extends RuntimeException {

    public OrderNotExistException(String message) {
        super(message);
    }

}
