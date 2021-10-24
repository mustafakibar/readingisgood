package kibar.readingisgood.order.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.time.LocalDateTime;

import kibar.readingisgood.order.data.model.Order;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

    Flux<Order> findAllByCustomerId(String customerId, Pageable pageable);

    @Query("{'createdAt' : { $gte: ?0, $lte: ?1 } }")
    Flux<Order> findTopByDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

}
