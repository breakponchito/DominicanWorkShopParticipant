package com.book.store.resource;

import com.book.store.model.Person;
import com.book.store.repository.PersonRepository;

import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.CursoredPage;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequestScoped
@Path("personas")
public class PersonasResource {
    
    private static final Logger logger = Logger.getLogger(PersonasResource.class.getName());
    
    @Inject
    PersonRepository personRepository;
    
    @GET
    @Path("findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findAll() {
        Stream<Person> personStream = personRepository.findAll();
        return personStream.collect(Collectors.toList());
    }
    
    @GET
    @Path("insertPersonasRandom/{quantity}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertPersonasRandom(@PathParam("quantity") int quantity) {
        List<String> firstNames = List.of(
                "Andrew", "Kalin", "Petr", "Alfonso", "Steve",
                "Chiara", "James", "Elif", "Luis", "Stian",
                "Arthur", "Fabio", "Gaurav", "Valentina", "Abdul"
        );
        List<String> lastNames = List.of(
                "Pielage", "Chan", "Aubrecht", "Valdez", "Millidge",
                "Civardi", "Hillyard", "Edman", "Neto", "Sigvartsen",
                "Malczuk", "Turizo", "Gupta", "Kovacic", "Rahim"
        );
        Random random = new Random();
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Person person = new Person();
            person.setFirstName(firstNames.get(random.nextInt(firstNames.size())));
            person.setLastName(lastNames.get(random.nextInt(lastNames.size())));
            person.setAge(random.nextInt(63) + 18);
            people.add(person);
        }

        List<Person> result = personRepository.insertPersonList(people);
        return Response.ok(result).build();
    }
    
    @GET
    @Path("/findPersonsAndMakePaginationWithOderOfName/{page}/{size}/{attribute}/{order}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPersonsAndMakePaginationWithOderOfName(
            @PathParam("page") int pageNumber, @PathParam("size") int size, 
            @PathParam("attribute") String attribute, @PathParam("order") String order) {
        Sort s = null;
        if (order.equals("asc")) {
            s = Sort.asc(attribute);
        } else {
            s = Sort.desc(attribute);
        }
        Page<Person> page = personRepository
                .findPersonsAndMakePaginationWithOrderOfName(PageRequest.ofPage(pageNumber).size(size), Order.by(s));
        
        logger.info("Page:"+page.toString());
        logger.info("Number of Elements:"+String.valueOf(page.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(page.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(page.totalElements()));
        logger.info("Total Pages:"+String.valueOf(page.totalPages()));
        return Response.ok(page.content()).build();
    }

    
    
    @GET
    @Path("/findPersonWithQueryAndMakePagination/{firstName}/{page}/{size}/{attribute}/{order}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPersonWithQueryAndMakePagination(@PathParam("firstName") String firstName, 
                                                         @PathParam("page") int pageNumber, 
                                                         @PathParam("size") int size,
                                                         @PathParam("attribute") String attribute,
                                                         @PathParam("order") String order) {
        Sort s = null;
        if (order.equals("asc")) {
            s = Sort.asc(attribute);
        } else {
            s = Sort.desc(attribute);
        }
        Page<Person> page = personRepository.findPersonWithQueryAndMakePagination(firstName, PageRequest.ofPage(pageNumber).size(size),
                Order.by(s));
        logger.info("Page:"+page.toString());
        logger.info("has next:"+page.hasNext());
        logger.info("Number of Elements:"+String.valueOf(page.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(page.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(page.totalElements()));
        logger.info("Total Pages:"+String.valueOf(page.totalPages()));

        if (page.hasNext()) {
            PageRequest p1 = page.nextPageRequest();
            Page<Person> page2 = personRepository.findPersonWithQueryAndMakePagination(firstName, PageRequest.ofPage(p1.page()).size(size),
                    Order.by(s));
            logger.info("Page:"+page2.toString());
            logger.info("previous:"+page2.hasPrevious());

            logger.info("Number of Elements:"+String.valueOf(page2.numberOfElements()));
            logger.info("Request Total:"+String.valueOf(page2.pageRequest().requestTotal()));
            logger.info("Total Elements:"+String.valueOf(page2.totalElements()));
            logger.info("Total Pages:"+String.valueOf(page2.totalPages()));
        }
        
        return Response.ok(page.content()).build();
    }

    @GET
    @Path("/findPersonPagWithCursorPage/{firstName}/{size}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPersonPagWithCursorPage(@PathParam("firstName") String firstName,
                                                @PathParam("size") int size) {
        Order<Person> order = Order.by(Sort.asc("age"));

        PageRequest pageRequest = PageRequest.ofSize(size);
        CursoredPage<Person> p = personRepository.findPersonPagWithCursorPage(firstName, pageRequest, order);
        logger.info("Page:"+p.toString());
        logger.info("Page Size:"+String.valueOf(p.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(p.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(p.totalElements()));
        logger.info("Total Pages:"+String.valueOf(p.totalPages()));
        logger.info("Content:"+p.content().toString());

        pageRequest = p.previousPageRequest();

        CursoredPage<Person> before = personRepository.findPersonPagWithCursorPage(firstName, pageRequest, order);
        logger.info("Page:"+before.toString());
        logger.info("Page Size:"+String.valueOf(before.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(before.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(before.totalElements()));
        logger.info("Total Pages:"+String.valueOf(before.totalPages()));
        logger.info("Content:"+before.content().toString());

        pageRequest = p.nextPageRequest();

        CursoredPage<Person> next = personRepository.findPersonPagWithCursorPage(firstName, pageRequest, order);
        logger.info("Page:"+next.toString());
        logger.info("Page Size:"+String.valueOf(next.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(next.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(next.totalElements()));
        logger.info("Total Pages:"+String.valueOf(next.totalPages()));
        logger.info("Content:"+next.content().toString());


        return Response.ok(p.content()).build();
    }

    @GET
    @Path("/findPersonPagWithCursorPageSpecificCursorCustomValue/{firstName}/{size}/{age}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPersonPagWithCursorPageSpecificCursorCustomValue(@PathParam("firstName") String firstName,
                                                                   @PathParam("size") int size, @PathParam("age") int age) {
        Order<Person> order = Order.by(Sort.asc("age"));
        PageRequest pageRequest = PageRequest.afterCursor(PageRequest.Cursor.forKey(age),1,1, true);

        CursoredPage<Person> p = personRepository.findPersonPagWithCursorPage(firstName, pageRequest, order);
        logger.info("Page:"+p.toString());
        logger.info("Page Size:"+String.valueOf(p.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(p.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(p.totalElements()));
        logger.info("Total Pages:"+String.valueOf(p.totalPages()));
        logger.info("Content:"+p.content().toString());

        pageRequest = p.previousPageRequest();

        CursoredPage<Person> before = personRepository.findPersonPagWithCursorPage(firstName, pageRequest, order);
        logger.info("Page:"+before.toString());
        logger.info("Page Size:"+String.valueOf(before.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(before.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(before.totalElements()));
        logger.info("Total Pages:"+String.valueOf(before.totalPages()));
        logger.info("Content:"+before.content().toString());

        pageRequest = p.nextPageRequest();

        CursoredPage<Person> next = personRepository.findPersonPagWithCursorPage(firstName, pageRequest, order);
        logger.info("Page:"+next.toString());
        logger.info("Page Size:"+String.valueOf(next.numberOfElements()));
        logger.info("Request Total:"+String.valueOf(next.pageRequest().requestTotal()));
        logger.info("Total Elements:"+String.valueOf(next.totalElements()));
        logger.info("Total Pages:"+String.valueOf(next.totalPages()));
        logger.info("Content:"+next.content().toString());
        return Response.ok(p.content()).build();
    }
    
}
