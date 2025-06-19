# Jakarta 11 with Payara 7 WorkShop

## Participant

### **Module 4: Add navigation view for the Book Catalog.**

Now is the time to add views for our catalog, until now we have the models we want to use to create a basic catalog application. Let's start to describe the view technologies for Jakarta EE 11. We have multiple options to create web applications on the Jakarta Side, here the list of some of them:

- Jakarta Servlets (version 6.1 for Jakarta 11)
- Jakarta Pages (version 4.0 for Jakarta 11)
- Jakarta JSF (version 4.1 for Jakarta 11)
- Jakarta MVC (version 3.0, not part of Jakarta 11, stand-alone specification)

In our case, we will focus on JSF to integrate our view to interact with our models.

#### Configure the application for JSF

To start, we need to add the structure of folder for web application and also include some configuration files. Here is the list of files that we need to include in our application and an example of the structure of folder to have:

- web.xml (here we will configure the JSF Servlet to resolve our views and the life cycle of JSF)
- beans.xml (this is to work with CDI beans, by default, the method to start CDI beans is the mode annotated)

![Structure of Folder](img\structureOfFolderWebApp.png)

We can start to configure our application to use JSF. To make that, we need to declare the Servlet that is going to resolve our views. You need to declare the following Servlet on your web.xml file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>Faces Servlet</servlet-name>
        <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>
</web-app>
```
To simplify this, I provided those files, and you can copy from the configuration folder from this module to you WEB-INF folder for your application.





