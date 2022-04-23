package com.codegym.cms.controller;

import com.codegym.cms.model.Book;
import com.codegym.cms.model.BookForm;
import com.codegym.cms.model.Category;
import com.codegym.cms.service.bookService.IBookService;
import com.codegym.cms.service.categoryService.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    @Value("${file-upload}")
    private String fileUpload;

    @ModelAttribute("category")
    public Iterable<Category> categories(){
        return categoryService.findAll();
    }

    @GetMapping("/create-book")
    public ModelAndView showCreateBookForm(){
        ModelAndView modelAndView = new ModelAndView("/book/create");
        modelAndView.addObject("book", new Book());
        return modelAndView;
    }
    @PostMapping("/create-book")
    public ModelAndView create(BookForm bookForm) {
        MultipartFile file = bookForm.getAvatar();
        //lay ten file
        String fileName = file.getOriginalFilename();

        //lay thong tin cua customer
        String name = bookForm.getName();
        double price = bookForm.getPrice();
        String author = bookForm.getAuthor();
        Category category = bookForm.getCategory();

        //coppy file
        try {
            FileCopyUtils.copy(file.getBytes(), new File(fileUpload+ fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Book book = new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), fileName, bookForm.getCategory());

        bookService.save(book);
        ModelAndView modelAndView = new ModelAndView("/book/create");
        modelAndView.addObject("message", "New book created successfully");
        return modelAndView;
    }

    @GetMapping("/books")
    public ModelAndView listBook(){
        Iterable<Book> books = bookService.findAll();
        ModelAndView modelAndView = new ModelAndView("/book/list");
        modelAndView.addObject("books", books);
        return modelAndView;
    }
    @GetMapping("/edit-book/{id}")
    public ModelAndView showEditBookForm(@PathVariable Long id){
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()){
            ModelAndView modelAndView = new ModelAndView("/book/edit");
            modelAndView.addObject("book", book.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("error.404");
            return modelAndView;
        }
    }
    @PostMapping("/edit-book")
    public ModelAndView updateBook(@ModelAttribute("book") Book book) {
        bookService.save(book);
        ModelAndView modelAndView = new ModelAndView("/book/edit");
        modelAndView.addObject("book", book);
        modelAndView.addObject("message", "Success!!! ");
        return modelAndView;
    }

    @GetMapping("/delete-book/{id}")
    public ModelAndView showDeleteBookForm(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/book/delete");
            modelAndView.addObject("book", book.get());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-book")
    public String deleteCustomer(@ModelAttribute("book") Book book) {
        bookService.remove(book.getId());
        return "redirect:books";
    }

}
