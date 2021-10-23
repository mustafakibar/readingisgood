package kibar.readingisgood.book.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import kibar.readingisgood.book.data.model.Book;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Mono<Boolean> existsByName(String name);

}
