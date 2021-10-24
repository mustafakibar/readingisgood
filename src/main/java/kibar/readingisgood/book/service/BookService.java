package kibar.readingisgood.book.service;

import org.springframework.stereotype.Service;

import kibar.readingisgood.book.data.model.Book;
import kibar.readingisgood.book.data.payload.AddBookRequest;
import kibar.readingisgood.book.exception.BookAlreadyExistException;
import kibar.readingisgood.book.exception.BookNotExistException;
import kibar.readingisgood.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Mono<Book> getById(String bookId) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BookNotExistException(String.format("Book with id '%s' not exist.", bookId)))));
    }

    public Flux<Book> getAll() {
        return bookRepository.findAll();
    }

    public Mono<Book> add(AddBookRequest addBookRequest) {
        return bookRepository.existsByName(addBookRequest.getName())
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.error(new BookAlreadyExistException(String.format("The book named '%s' is already exist.", addBookRequest.getName())));
                    }

                    Book book = Book.builder()
                            .name(addBookRequest.getName())
                            .stock(addBookRequest.getStock())
                            .build();

                    return bookRepository.save(book);
                });
    }

    public Mono<Book> updateStock(String bookId, Long amount) {
        return getById(bookId)
                .flatMap(book -> {
                    book.setStock(amount);
                    return bookRepository.save(book);
                });
    }

}
