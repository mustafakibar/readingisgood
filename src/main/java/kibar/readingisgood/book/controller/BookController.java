package kibar.readingisgood.book.controller;

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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import kibar.readingisgood.book.data.model.Book;
import kibar.readingisgood.book.data.payload.AddBookRequest;
import kibar.readingisgood.book.service.BookService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = {"*"})
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Book>> getById(@PathVariable(value = "id") String bookId) {
        return ResponseEntity.ok(bookService.getById(bookId));
    }

    @GetMapping
    public ResponseEntity<Flux<Book>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @PostMapping
    public ResponseEntity<Mono<Book>> add(@Valid @RequestBody AddBookRequest addBookRequest) {
        return ResponseEntity.ok(bookService.add(addBookRequest));
    }

    @PutMapping(value = "/{id}", params = "stock")
    public ResponseEntity<Mono<Book>> updateStock(
            @PathVariable(value = "id") String bookId,
            @Valid @NotNull @PositiveOrZero @RequestParam(value = "stock") Long stock) {
        return ResponseEntity.ok(bookService.updateStock(bookId, stock));
    }

}
