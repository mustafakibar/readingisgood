package kibar.readingisgood.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public final class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }

}
