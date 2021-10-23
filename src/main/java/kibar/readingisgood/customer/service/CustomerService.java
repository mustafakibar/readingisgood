package kibar.readingisgood.customer.service;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kibar.readingisgood.customer.data.model.Customer;
import kibar.readingisgood.customer.data.payload.AddCustomerRequest;
import kibar.readingisgood.customer.data.payload.GetAllOrderByCustomerIdRequest;
import kibar.readingisgood.customer.exception.CustomerAlreadyExistException;
import kibar.readingisgood.customer.exception.CustomerNotExistException;
import kibar.readingisgood.customer.repository.CustomerRepository;
import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.payload.ListOrderByCustomerIdRequest;
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

    public Mono<Customer> getById(String id) {
        return customerRepository.findById(id)
                .onErrorMap(throwable -> new CustomerNotExistException(String.format("Customer with id '%s' not exist.", id)));
    }

    public Mono<Customer> getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .onErrorMap(throwable -> new CustomerNotExistException(String.format("Customer with email '%s' not exist.", email)));
    }

    public Flux<Page<Order>> getOrdersByCustomerId(GetAllOrderByCustomerIdRequest getAllOrderByCustomerIdRequest) {
        return getById(getAllOrderByCustomerIdRequest.getId())
                .flatMapMany(_ignore -> orderService.getAllByCustomerId(new ListOrderByCustomerIdRequest(getAllOrderByCustomerIdRequest.getId(), getAllOrderByCustomerIdRequest.getPageRequest())));
    }

    public Flux<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Mono<Customer> add(AddCustomerRequest addCustomerRequest) {
        return customerRepository.findByEmail(addCustomerRequest.getMail())
                .switchIfEmpty(Mono.defer(() -> {
                    Customer customer = Customer.builder()
                            .name(addCustomerRequest.getName())
                            .mail(addCustomerRequest.getMail())
                            .password(bCryptPasswordEncoder.encode(addCustomerRequest.getPassword()))
                            .build();

                    return Mono.just(customer);
                }))
                .flatMap(customer -> Mono.error(new CustomerAlreadyExistException("The email address you have entered is already registered")));
    }

}
