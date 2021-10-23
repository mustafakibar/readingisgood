package kibar.readingisgood.order.service;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import kibar.readingisgood.book.data.model.Book;
import kibar.readingisgood.book.data.payload.UpdateBookStockRequest;
import kibar.readingisgood.book.service.BookService;
import kibar.readingisgood.customer.data.model.Customer;
import kibar.readingisgood.customer.exception.CustomerNotExistException;
import kibar.readingisgood.customer.service.CustomerService;
import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.model.OrderStatus;
import kibar.readingisgood.order.data.payload.CreateOrderRequest;
import kibar.readingisgood.order.data.payload.ListOrderByDateRequest;
import kibar.readingisgood.order.data.payload.UpdateOrderStatusRequest;
import kibar.readingisgood.order.exception.ListOrderBetweenDateInvalidInputException;
import kibar.readingisgood.order.exception.OutOfStockException;
import kibar.readingisgood.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    private final BookService bookService;
    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    public Mono<Order> getById(String id) {
        return orderRepository.findById(id)
                .onErrorMap(throwable -> new CustomerNotExistException(String.format("Order with id '%s' not exist.", id)));
    }

    public Flux<Order> getAll() {
        return orderRepository.findAll();
    }

    public Flux<Order> getAllBetweenDates(ListOrderByDateRequest listOrderByDateRequest) {
        Date start = listOrderByDateRequest.getFrom();
        Date end = listOrderByDateRequest.getTo();

        if (end.before(start)) {
            // Note: tF for date format, tT for time format, and, %1 and %2 for indexing
            String message = String.format("End date '%2$tF-%2$tT' can not be less than start date '%1$tF-%1$tT'", start, end);
            return Flux.error(new ListOrderBetweenDateInvalidInputException(message));
        }

        return orderRepository.findByDateBetween(listOrderByDateRequest.getFrom(), listOrderByDateRequest.getTo());
    }

    @Transactional
    public Mono<Order> create(CreateOrderRequest createOrderRequest) {
        return bookService.getById(createOrderRequest.getBookId())
                .filter(book -> book.getStock() >= createOrderRequest.getAmount())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new OutOfStockException("The product you ordered is out of stock"))))
                .flatMap(book -> bookService.updateStock(new UpdateBookStockRequest(createOrderRequest.getBookId(), book.getStock() - createOrderRequest.getAmount())))
                .doOnError(throwable -> {}) // stock update hatası logla
                .flatMap(book -> customerService.getById(createOrderRequest.getCustomerId()).map(customer -> Pair.of(book, customer)))
                .flatMap(pair -> {
                    Book book = pair.getFirst();
                    Customer customer = pair.getSecond();

                    Order order = Order.builder()
                            .bookId(book.getId())
                            .customerId(customer.getId())
                            .amount(createOrderRequest.getAmount())
                            .status(OrderStatus.INPROGRESS)
                            .build();

                    return orderRepository.save(order);
                });
    }

    @Transactional
    public Mono<Order> updateStatus(UpdateOrderStatusRequest updateOrderStatusRequest) {
        return getById(updateOrderStatusRequest.getId())
                .flatMap(order -> {
                    if (order.getStatus() == updateOrderStatusRequest.getOrderStatus()) {
                        return Mono.just(order);
                    } else {
                        order.setStatus(updateOrderStatusRequest.getOrderStatus());

                        Mono<Order> result;
                        if (updateOrderStatusRequest.getOrderStatus() == OrderStatus.REJECTED) {
                            result = bookService.getById(order.getBookId())
                                    .flatMap(book -> bookService.updateStock(new UpdateBookStockRequest(book.getId(), Long.sum(book.getStock(), order.getAmount()))))
                                    .map(book -> order);
                        } else {
                            result = Mono.just(order);
                        }

                        return result.flatMap(orderRepository::save);

                    }
                });
    }

}
