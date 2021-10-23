package kibar.readingisgood.customer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import kibar.readingisgood.customer.data.model.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Customer> findByEmail(String email);

}
