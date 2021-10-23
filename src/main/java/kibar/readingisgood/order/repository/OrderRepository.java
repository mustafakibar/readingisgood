package kibar.readingisgood.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.Date;

import kibar.readingisgood.order.data.model.Order;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

    Flux<Page<Order>> findByCustomerId(String customerId, Pageable pageable);

    Flux<Order> findByDateBetween(Date from, Date to);

}
