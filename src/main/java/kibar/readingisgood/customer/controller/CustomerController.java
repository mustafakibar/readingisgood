package kibar.readingisgood.customer.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import kibar.readingisgood.customer.data.model.Customer;
import kibar.readingisgood.customer.data.payload.AddCustomerRequest;
import kibar.readingisgood.customer.data.payload.GetAllOrdersByCustomerIdRequest;
import kibar.readingisgood.customer.service.CustomerService;
import kibar.readingisgood.order.data.model.Order;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = {"*"})
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Customer>> getById(@PathVariable(value = "id") String customerId) {
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @GetMapping(params = "email")
    public ResponseEntity<Mono<Customer>> getByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(customerService.getByEmail(email));
    }

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<Flux<Order>> getAllOrdersById(
            @PathVariable(value = "id") String customerId,
            @RequestParam(defaultValue = "0", value = "page", required = false) Integer page,
            @RequestParam(defaultValue = "9", value = "size", required = false) Integer size) {
        var listOrderByCustomerIdRequest = GetAllOrdersByCustomerIdRequest.builder()
                .id(customerId)
                .pageRequest(PageRequest.of(page, size))
                .build();

        return ResponseEntity.ok(customerService.getOrdersByCustomerId(listOrderByCustomerIdRequest));
    }

    @GetMapping
    public ResponseEntity<Flux<Customer>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PostMapping
    public ResponseEntity<Mono<Customer>> add(@Valid @RequestBody AddCustomerRequest addCustomerRequest) {
        return ResponseEntity.ok(customerService.add(addCustomerRequest));
    }

}
