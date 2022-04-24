package com.codegym.cms.repo;


import com.codegym.cms.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICategoryRepository extends PagingAndSortingRepository<Category,Long> {

}