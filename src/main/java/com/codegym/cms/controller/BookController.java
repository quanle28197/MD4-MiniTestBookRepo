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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {
    @Value("${file-upload}")
    private String fileUpload;

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories(){
        return categoryService.findAll();
    }

    @GetMapping()
    public ModelAndView listBook(@RequestParam("search")Optional<String> search){
        Iterable<Book> books;
        if (search.isPresent()){
            books = bookService.findAllByNameContaining(search.get());
            ModelAndView modelAndView = new ModelAndView("/book/list");
            modelAndView.addObject("back","Back to book list");
            modelAndView.addObject("books",books);
            return modelAndView;
        } else {
            books = bookService.findAll();
            ModelAndView modelAndView = new ModelAndView("/book/list");
            modelAndView.addObject("books",books);
            return modelAndView;
        }
    }
    @GetMapping("/create")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/book/create");
        modelAndView.addObject("books",new BookForm());
        return modelAndView;
    }
    @PostMapping("/save")
    public ModelAndView saveBook(@ModelAttribute("books") BookForm bookForm){
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try{
            FileCopyUtils.copy(bookForm.getImage().getBytes(),new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book = new Book(bookForm.getName(),bookForm.getPrice(),bookForm.getAuthor(),fileName,bookForm.getCategory());
        bookService.save(book);
        ModelAndView modelAndView = new ModelAndView("/book/create");
        modelAndView.addObject("book",book);
        modelAndView.addObject("message","created successfully!!!");
        return modelAndView;
    }
    @GetMapping("/edit/{id}")
    public ModelAndView showFormEdit(@PathVariable Long id){
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()){
            ModelAndView modelAndView = new ModelAndView("/book/edit");
            modelAndView.addObject("book",bookOptional.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/book/edit");
            modelAndView.addObject("message","Error");
            return modelAndView;
        }
    }
    @PostMapping("/update")
    public ModelAndView updateBookInformation(@ModelAttribute("book")BookForm bookForm){
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(bookForm.getImage().getBytes(),new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book = new Book(bookForm.getName(),bookForm.getPrice(),bookForm.getAuthor(),fileName,bookForm.getCategory());
        bookService.remove(bookForm.getId());
        bookService.save(book);
        ModelAndView modelAndView = new ModelAndView("/book/edit");
        modelAndView.addObject("message","Updated Success");
        modelAndView.addObject("book",book);
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id){
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()){
            ModelAndView modelAndView = new ModelAndView("/book/delete");
            modelAndView.addObject("book",optionalBook.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/book/delete");
            modelAndView.addObject("message","Error");
            return modelAndView;
        }
    }
    @PostMapping("/delete")
    public ModelAndView deleteBook(@ModelAttribute("book")Book book){
        bookService.remove(book.getId());
        ModelAndView modelAndView = new ModelAndView("/book/list");
        modelAndView.addObject("books",bookService.findAll());
        return modelAndView;
    }
    @GetMapping("/view/{id}")
    public ModelAndView showBookView(@PathVariable Long id){
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()){
            Iterable<Book> books = bookService.findAllByCategory(optionalBook.get().getCategory());
            ModelAndView modelAndView = new ModelAndView("/book/view");
            modelAndView.addObject("bookView",optionalBook.get());
            modelAndView.addObject("books",books);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("book/list");
            modelAndView.addObject("books",bookService.findAll());
            modelAndView.addObject("message","Can't View Book");
            return modelAndView;
        }
    }
}
