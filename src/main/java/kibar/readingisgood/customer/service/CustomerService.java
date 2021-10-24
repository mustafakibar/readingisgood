package kibar.readingisgood.customer.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kibar.readingisgood.customer.data.model.Customer;
import kibar.readingisgood.customer.data.payload.AddCustomerRequest;
import kibar.readingisgood.customer.data.payload.GetAllOrdersByCustomerIdRequest;
import kibar.readingisgood.customer.exception.CustomerAlreadyExistException;
import kibar.readingisgood.customer.exception.CustomerNotExistException;
import kibar.readingisgood.customer.repository.CustomerRepository;
import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.payload.ListOrdersByCustomerIdRequest;
import kibar.readingisgood.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<Customer> getById(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CustomerNotExistException(String.format("Customer with id '%s' not exist.", customerId)))));
    }

    public Mono<Customer> getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CustomerNotExistException(String.format("Customer with email '%s' not exist.", email)))));
    }

    public Flux<Order> getOrdersByCustomerId(GetAllOrdersByCustomerIdRequest getAllOrdersByCustomerIdRequest) {
        return getById(getAllOrdersByCustomerIdRequest.getId())
                .flatMapMany(_ignore -> orderService.getAllByCustomerId(new ListOrdersByCustomerIdRequest(getAllOrdersByCustomerIdRequest.getId(), getAllOrdersByCustomerIdRequest.getPageRequest())));
    }

    public Flux<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Mono<Customer> add(AddCustomerRequest addCustomerRequest) {
        return customerRepository.findByEmail(addCustomerRequest.getMail())
                .flatMap(customer -> Mono.error(new CustomerAlreadyExistException("The email address you have entered is already registered")))
                .switchIfEmpty(Mono.defer(() -> {
                    Customer customer = Customer.builder()
                            .name(addCustomerRequest.getName())
                            .email(addCustomerRequest.getMail())
                            .password(bCryptPasswordEncoder.encode(addCustomerRequest.getPassword()))
                            .build();

                    return customerRepository.save(customer);
                }))
                .cast(Customer.class);
    }

}
