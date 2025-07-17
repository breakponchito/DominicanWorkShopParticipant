package fish.payara.dominican.workshop.dominicanworkshop.views;

import fish.payara.dominican.workshop.dominicanworkshop.entities.Author;
import fish.payara.dominican.workshop.dominicanworkshop.entities.Book;
import fish.payara.dominican.workshop.dominicanworkshop.service.CatalogService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;


@ViewScoped
@Named("bookView")
public class BookView implements Serializable {

    private Book book = new Book();
    private Author author = new Author();
    
    @Inject
    private transient CatalogService catalogService;
    
    public Book getBook() {
        return book;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public List<Book> getBooks() {
        return catalogService.getAllBooks();
    }
    
    public  String saveBook(){
        book.setAuthor(author);
        
        if(book.getId() == null) {
            catalogService.createBook(book);
        } else {
            catalogService.editBook(book);
        }
        book = new Book();
        author = new Author();
        return "book";
    }
    
    public String deleteBook(Book book) {
        catalogService.deleteBook(book.getId());
        catalogService.deleteAuthor(book.getAuthor().getId());
        return "book";
    }
    
    public String editBook(Book book) {
        this.book = book;
        this.author = book.getAuthor();
        return null;
    }
}
