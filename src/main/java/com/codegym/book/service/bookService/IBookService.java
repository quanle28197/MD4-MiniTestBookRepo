package com.codegym.book.service.bookService;

import com.codegym.book.model.Book;
import com.codegym.book.model.Category;
import com.codegym.book.service.IGeneralService;

public interface IBookService extends IGeneralService<Book> {
    Iterable<Book> findAllByCategory(Category category);
    Iterable<Book> findAllByNameContaining(String name);
}
