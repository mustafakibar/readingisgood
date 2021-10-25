package kibar.readingisgood.order.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import javax.validation.Valid;

import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.model.OrderStatus;
import kibar.readingisgood.order.data.payload.CreateOrderRequest;
import kibar.readingisgood.order.data.payload.ListOrderByDateRequest;
import kibar.readingisgood.order.data.payload.ListOrdersByCustomerIdRequest;
import kibar.readingisgood.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = {"*"})
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Order>> getById(@PathVariable(value = "id") String orderId) {
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @GetMapping(params = "customerId")
    public ResponseEntity<Flux<Order>> getAllByCustomerId(
            @RequestParam(value = "customerId") String customerId,
            @RequestParam(defaultValue = "0", value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "9", value = "size", required = false) Integer size) {
        var listOrdersByCustomerIdRequest = ListOrdersByCustomerIdRequest.builder()
                .id(customerId)
                .pageRequest(PageRequest.of(page, size))
                .build();

        return ResponseEntity.ok(orderService.getAllByCustomerId(listOrdersByCustomerIdRequest));
    }

    @GetMapping(params = "from")
    public ResponseEntity<Flux<Order>> filterByDate(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0", value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "9", value = "size", required = false) Integer size
    ) {
        var listOrderByDateRequest = ListOrderByDateRequest.builder()
                .from(from)
                .to(to)
                .pageRequest(PageRequest.of(page, size))
                .build();

        return ResponseEntity.ok(orderService.getAllBetweenDates(listOrderByDateRequest));
    }

    @GetMapping
    public ResponseEntity<Flux<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PostMapping
    public ResponseEntity<Mono<Order>> create(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.create(createOrderRequest));
    }

    @PutMapping(value = "/{id}", params = "status")
    public ResponseEntity<Mono<Order>> updateStatus(
            @PathVariable(value = "id") String orderId,
            @RequestParam("status") String orderStatus) {
        try {
            return ResponseEntity.ok(orderService.updateStatus(orderId, OrderStatus.valueOf(orderStatus)));
        } catch (IllegalArgumentException e /* Catch if OrderStatus has no constant given name */) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal enum constant!");
        }
    }

}
