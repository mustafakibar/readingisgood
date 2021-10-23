package kibar.readingisgood.book.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import kibar.readingisgood.book.service.BookService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"*"})
@Validated
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

}
