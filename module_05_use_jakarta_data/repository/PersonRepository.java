package com.book.store.repository;

import com.book.store.model.Person;

import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.CursoredPage;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.By;
import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import java.util.List;
import java.util.stream.Stream;


@Repository
public interface PersonRepository {
    
    @Find
    Stream<Person> findAll();
    
    @Insert
    List<Person> insertPersonList(List<Person> personList);
    
    @Find
    Page<Person> findPersonsAndMakePaginationWithOrderOfName(PageRequest pageRequest, Order<Person> order);
    
    @Query("FROM Person where firstName like :firstName")
    Page<Person> findPersonWithQueryAndMakePagination(@Param("firstName")String firstName, PageRequest pageRequest, Order<Person> order);

    @Find
    CursoredPage<Person> findPersonPagWithCursorPage(@By("firstName") String name,
                                                     PageRequest pageRequest,
                                                     Order<Person> sorts);
}
