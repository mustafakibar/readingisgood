package kibar.readingisgood.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import kibar.readingisgood.book.service.BookService;
import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.model.OrderStatus;
import kibar.readingisgood.order.data.payload.CreateOrderRequest;
import kibar.readingisgood.order.data.payload.ListOrderByDateRequest;
import kibar.readingisgood.order.data.payload.ListOrdersByCustomerIdRequest;
import kibar.readingisgood.order.exception.ListOrderBetweenDateInvalidInputException;
import kibar.readingisgood.order.exception.OrderNotExistException;
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

    public Mono<Order> getById(String orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new OrderNotExistException(String.format("Order with id '%s' not exist.", orderId)))));
    }

    public Flux<Order> getAllByCustomerId(ListOrdersByCustomerIdRequest listOrdersByCustomerIdRequest) {
        return orderRepository.findAllByCustomerId(listOrdersByCustomerIdRequest.getId(), listOrdersByCustomerIdRequest.getPageRequest())
                .switchIfEmpty(Flux.defer(() -> Flux.error(new OrderNotExistException("User does not have any orders"))));
    }

    public Flux<Order> getAll() {
        return orderRepository.findAll();
    }

    public Flux<Order> getAllBetweenDates(ListOrderByDateRequest listOrderByDateRequest) {
        LocalDateTime start = listOrderByDateRequest.getFrom();
        LocalDateTime end = listOrderByDateRequest.getTo();

        if (end.isBefore(start)) {
            // Note: tF: ISO date format, tT: ISO time format, %1$ and %2$ for indexing
            String message = String.format("End date '%2$tF-%2$tT' can not be less than start date '%1$tF-%1$tT'", start, end);
            return Flux.error(new ListOrderBetweenDateInvalidInputException(message));
        }

        return orderRepository.findTopByDateBetween(listOrderByDateRequest.getFrom(), listOrderByDateRequest.getTo(), listOrderByDateRequest.getPageRequest());
    }

    @Transactional
    public Mono<Order> create(CreateOrderRequest createOrderRequest) {
        return bookService.getById(createOrderRequest.getBookId())
                .filter(book -> book.getStock() >= createOrderRequest.getAmount())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new OutOfStockException("The product you ordered is out of stock"))))
                .flatMap(book -> bookService.updateStock(createOrderRequest.getBookId(), book.getStock() - createOrderRequest.getAmount()))
                .doOnError(throwable -> {}) // stock update hatası logla
                .flatMap(book -> {
                    Order order = Order.builder()
                            .bookId(createOrderRequest.getBookId())
                            .customerId(createOrderRequest.getCustomerId())
                            .amount(createOrderRequest.getAmount())
                            .status(OrderStatus.INPROGRESS)
                            .build();

                    return orderRepository.save(order);
                });
    }

    @Transactional
    public Mono<Order> updateStatus(String orderId, OrderStatus newOrderStatus) {
        return getById(orderId)
                .flatMap(order -> {
                    if (order.getStatus() == newOrderStatus) {
                        return Mono.just(order);
                    } else {
                        order.setStatus(newOrderStatus);

                        Mono<Order> result;
                        if (newOrderStatus == OrderStatus.REJECTED) {
                            result = bookService.getById(order.getBookId())
                                    .flatMap(book -> bookService.updateStock(book.getId(), Long.sum(book.getStock(), order.getAmount())))
                                    .map(book -> order);
                        } else {
                            result = Mono.just(order);
                        }

                        return result.flatMap(orderRepository::save);
                    }
                });
    }

}
