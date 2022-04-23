package com.codegym.cms.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    private double price;

    private String author;

    private String avatar;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Book(Long id, String name, double price, String author, String avatar) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.author = author;
        this.avatar = avatar;
    }

    public Book(String name, double price, String author, String avatar, Category category) {
        this.name = name;
        this.price = price;
        this.author = author;
        this.avatar = avatar;
        this.category = category;
    }

    public Book() {
    }

    public Book(String name, int price, String author) {
        this.name = name;
        this.price = price;
        this.author = author;
    }

    public Book(Long id, String name, double price, String author, Category category, String fileName) {
    }

    @Override
    public String toString() {
        return String.format("Book[id=%d, name='%s', price='%s', author='%s']", id,name, price, author);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
