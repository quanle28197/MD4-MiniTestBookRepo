package com.codegym.book.repo;

import com.codegym.book.model.Book;
import com.codegym.book.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends PagingAndSortingRepository<Book,Long> {
    Iterable<Book> findAllByCategory(Category category);
    Iterable<Book> findAllByNameContaining(String name);
}
