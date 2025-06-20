package fish.payara.dominican.workshop.dominicanworkshop.service;

import fish.payara.dominican.workshop.dominicanworkshop.entities.Address;
import fish.payara.dominican.workshop.dominicanworkshop.entities.Author;
import fish.payara.dominican.workshop.dominicanworkshop.entities.Book;
import fish.payara.dominican.workshop.dominicanworkshop.entities.BookDTO;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Named
public class AdminService {
    
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());

    @PersistenceContext(unitName = "pu1")
    private EntityManager em;

    @Resource
    private UserTransaction utx;
    
    @Inject
    private BookView bookView;
    
    @Inject
    private AddressView addressView;
    
    @Inject
    private AuthorView authorView;
    
    @Inject
    CatalogService catalogService;
    
    public String addBook() {
        try{
            utx.begin();
            Book book = new Book();
            book.setTitle(bookView.getTitle());
            book.setDescription(bookView.getDescription());
            book.setPrice(bookView.getPrice());
            book.setImageName(bookView.getImageName());
            book.setIsbn(bookView.getIsbn());
            book.setPublicationDate(bookView.getPublicationDate());
            Author author = new Author();
            author.setName(authorView.getName());
            author.setAge(authorView.getAge());
            Address address = new Address(addressView.getStreet(), addressView.getCity(), addressView.getState(), addressView.getZip());
            author.setAddress(address);
            book.setAuthor(author);
            em.persist(book);
            utx.commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "catalogAdmin";
    }
    
    public String removeBook() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            utx.begin();
            Book bookToRemove = em.find(Book.class, Long.valueOf(params.get("id")));
            em.remove(bookToRemove);
            em.flush();
            utx.commit();
            
            if (bookToRemove != null && this.catalogService.getAllBooks() != null) {
                this.catalogService.getAllBooks().remove(bookToRemove);
            }
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return "menu";
    }
    
    public String editBook() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Book book = em.find(Book.class, Long.valueOf(params.get("id")));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("editBook", book);
        this.bookView.setTitle(book.getTitle());
        this.bookView.setDescription(book.getDescription());
        this.bookView.setPrice(book.getPrice());
        this.bookView.setImageName(book.getImageName());
        this.bookView.setIsbn(book.getIsbn());
        this.bookView.setPublicationDate(book.getPublicationDate());
        this.authorView.setName(book.getAuthor().getName());
        this.authorView.setAge(book.getAuthor().getAge());
        this.addressView.setStreet(book.getAuthor().getAddress().street());
        this.addressView.setCity(book.getAuthor().getAddress().city());
        this.addressView.setState(book.getAuthor().getAddress().state());
        this.addressView.setZip(book.getAuthor().getAddress().zip());
        return "editBook";
    }
    
    public String updateBook() {
        Book book = (Book) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("editBook");
        book.setTitle(bookView.getTitle());
        book.setDescription(bookView.getDescription());
        book.setPrice(bookView.getPrice());
        book.setImageName(bookView.getImageName());
        book.setIsbn(bookView.getIsbn());
        book.setPublicationDate(bookView.getPublicationDate());
        book.getAuthor().setName(authorView.getName());
        book.getAuthor().setAge(authorView.getAge());
        Address address = new Address(addressView.getStreet(), addressView.getCity(), addressView.getState(), addressView.getZip());
        book.getAuthor().setAddress(address);
        try {
            utx.begin();
            em.merge(book);
            utx.commit();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editBook");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return "catalogAdmin";
    }
}
