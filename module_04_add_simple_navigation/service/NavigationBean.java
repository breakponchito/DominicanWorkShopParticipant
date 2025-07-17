package fish.payara.dominican.workshop.dominicanworkshop.views;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class NavigationBean implements Serializable {
    private String activePage = "menu";

    public String getActivePage() { return activePage; }
    public void setActivePage(String activePage) { this.activePage = activePage; }

    public String goTo(String page) {
        this.activePage = page;
        return page + "?faces-redirect=true";
    }
}