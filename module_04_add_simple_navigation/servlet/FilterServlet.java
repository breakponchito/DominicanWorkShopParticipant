package fish.payara.dominican.workshop.dominicanworkshop.servlet;

import jakarta.faces.application.ResourceHandler;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(servletNames = {"Faces Servlet"})
public class FilterServlet implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if (!req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { 
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            res.setHeader("Pragma", "no-cache"); 
            res.setDateHeader("Expires", 0); 
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
