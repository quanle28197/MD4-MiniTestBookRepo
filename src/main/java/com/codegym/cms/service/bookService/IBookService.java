package com.codegym.cms.service.bookService;

import com.codegym.cms.model.Book;
import com.codegym.cms.model.Category;
import com.codegym.cms.service.IGeneralService;

public interface IBookService extends IGeneralService<Book> {
    Iterable<Book> findAllByCategory(Category category);
}
