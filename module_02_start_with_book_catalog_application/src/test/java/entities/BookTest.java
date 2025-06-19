package fish.payara.dominican.workshop.dominicanworkshop.entities;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {
    
    private EntityManagerFactory entityManagerFactory;
    
    @BeforeEach
    void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("integration-test");
    }
    
    @AfterEach
    void destroy() {
        entityManagerFactory.close();
    }
    
    @Test
    void testSelect() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Book book = new Book();
        book.setTitle("Spring  AI in Action");
        book.setAuthor("Craig Walls");
        book.setIsbn("978-0596152657");
        book.setDescription("Generative AI tools like ChatGPT cause an immediate jaw drop for almost everyone who encounters them.");
        book.setPublicationDate(LocalDate.of(2024, 8, 1));
        book.setPrice(35.00);
        book.setImageName("imageBook1.png");
        
        entityManager.persist(book);
        entityManager.getTransaction().commit();
        List<Book> list = entityManager.createQuery("select b from Book b", Book.class).getResultList();
        assertEquals(list.size(), 1);
    }

}