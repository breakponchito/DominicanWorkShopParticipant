# Jakarta 11 with Payara 7 WorkShop

## Participant

### **Module 3: Expose api rest.**

#### Introduction to Jakarta RESTful Web Service

Representational State Transfer (REST) is an architectural style where the Web Services are viewed as resources, and ca be identified by Uniform Resources Identifiers (URIs). The Web Services implemented using REST are named RESTful Web Services. With Jakarta EE, the specification that defines this style is Jakarta RESTful Web Services. For Jakarta 11 now we are on the version 4.0.

For Jakarta RESTful Web Services 4.0 we have multiple implementations showed on the following table:

| Name           | version     |
|----------------|-------------|
| RESTEasy       | 7.0.0.Beta1 |
| Eclipse Jersey | 4.0.0-M2    |

---
**NOTE**
This list is not final because this spec was recently released for Jakarta 11 and other new implementations can be listed in feature. Payara Server is using Eclipse Jersey because is the reference implementation.

---

The REST architectural style is designed to use a stateless communication protocol, typically HTTP. The following principles encourage RESTful applications to be simple, lightweight, and fast:

- **Resource identification through URI**: A RESTful web service exposes a set of resources that identify the targets of the interaction with its clients. Resources are identified by URIs, which provide a global addressing space for resource and service discovery,
- **Uniform Interface**: Resources are manipulated using a fixed set of operations described by the protocol. Check the table on the next topic to see the full list of methods available.
- **Self-description messages**: Resources are decoupled from their representation so that their content can be accessed in a variety of formats, such as HTML, XML, plain text, PDF, JPEG, JSON, and other document formats.
- Stateful interactions through links: Every interaction with a resource is stateless; that is, request messages are self-contained. Stateful interactions are based on the concept of explicit state transfer.

To understand RESTful Web Services we need to introduce it to the semantics defined to use it. The base operations we can make with RESTful Web Services are defined in the following table:

| Method  | Description                                                                                                    |
|---------|----------------------------------------------------------------------------------------------------------------|
| GET     | A ***GET*** request is used to retrieve an existing resource.                                                  |
| POST    | A ***POST*** request is used to create a new resource.                                                         |
| PUT     | A ***PUT*** request is used to update an existing resource. If the resource doesn't exists, it may be created. |
| DELETE  | A ***DELETE*** request is used to delete a resource.                                                           |
| HEAD    | A ***HEAD*** request returns an HTTP header with no body.                                                      |
| PATCH   | A ***PATCH*** request is used for partial resource modification.                                               |
| OPTIONS | A ***OPTIONS*** request retrieves a communication options available for a resource.                            |

Another part that is important is the type of content managed by this kind of service. Commonly this kind of service is associated with JSON (JavaScript Object Notation), but we can change using something named MIME types. The MIME stands for Multipurpose Internet Mail Extensions; it is used to indicate the type of data to be produced or consumed by the service. We will see how to use later with examples.

#### Developing simple Jakarta RESTful Web Service

When developing our RESTful Web Services, it is important to access the component. To make that, we are going to use the annotation ***@Path***. With this annotation, we are marking a class or method to be identified as a resource and therefore be accesible using URI's. After this, we need to indicate which methods this resource will provide using other annotations to handle it. The following table shows each of those annotations to use:

| Annotation | Method                                                                                                                            |
|------------|-----------------------------------------------------------------------------------------------------------------------------------|
| @GET       | Used to decorate a method to indicate that this method will retrieve an existing resource.                                        |
| @POST      | Used to decorate a method to indicate that this method will create a new resource.                                                |
| @PUT       | Used to decorate a method to indicate that this method will update an existing resource or create if the resource doesn't exists. |
| @DELETE    | Used to decorate a method to indicate that this method will delete an existing resource.                                          |
| @HEAD      | Used to decorate a method to indicate that this method will return an HTTP header with no body.                                   |
| @PATCH     | Used to decorate a method to indicate that this method will modify partially a resource.                                          |
| @OPTIONS   | Used to decorate a method to indicate that this method will retrieve a communication options available for a resource.            |

Another important part for the service is to indicate which kind of content will produce and consume. To indicate this, we have other two annotations that can help us with this. The following annotations ***@Produces*** and ***@Consumes*** will help us to indicate what kind of content we are going to use. For both annotations, we need to indicate the content type, and we can use the MediaType class to indicate using some constants defined on this class that represents the specific type to be managed by the service. The following table shows the most common, but you can check the complete definition here: [MediaType API](https://jakarta.ee/specifications/restful-ws/4.0/apidocs/jakarta.ws.rs/jakarta/ws/rs/core/mediatype)

| MediaType                  | Definition                                              |
|----------------------------|---------------------------------------------------------|
| MediaType.TEXT_PLAIN       | this value represents the media type "text/plain"       |
| MediaType.TEXT_HTML        | this value represents the media type "text/html"        |
| MediaType.APPLICATION_JSON | this value represents the media type "application/json" |
| MediaType.APPLICATION_XML  | this value represents the media type "application/xml"  |

Here is an example of code to show a simple service that is returning and consuming simple text:

```java 
@Path("/hello-world")
public class HelloWorldResource {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello World!";
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String sendMessage(String message) {
        return "echo: "+message;
    }
}
```

To call this service, we need to provide full url for the application. Suppose we deployed our application with the context root **myapplication** and that we defined our REST resources path for our application as **api**, then we need to include the protocol, server and port to access the service. The following will be the endpoint to access:

```http request
http://localhost:8080/myapplication/api/
```

We can use cURL to consume service, the browser or another tool like [Postman](https://www.postman.com/) to test it.

As an example, if I select to use cURL to test it, the following will be the call for each of the endpoints:

To access, GET method
```http request
curl http://localhost:8080/myapplication/api/hello-world
```
To access POST method
```http request
curl -X POST -d "my name is Alfonso" -H "Content-Type: text/plain" http://localhost:8080/myapplication/api/hello-world
```

#### Configuring REST resources path for our application

In the previous example, we said that for our service we assume our REST resource path as **api** to configure this we need to provide a class that extends **jakarta.ws.rs.core.Application** and must be annotated with @ApplicationPath and for the annotation we need to indicate the value for the desired REST resources path. The following example shows the case:

```java
@ApplicationPath("/api")
public class BookStoreApplication extends Application {

}
```

This configuration component doesn't need to specify any method and can be located in any package and defined only one time.

-----
#### **Task**

Now is the time to experiment. From the base application we are implementing create the first RESTful Web Service providing all the components that are required to make it work. Remember that by default, The Jakarta Starter creates one resource and configuration for a REST service. Review those components and edit as required, also copy the example we showed here to make a simple GET and POST call.
-----

#### Defining Client application

We tested the first endpoint using the browser for get methods and curl for post. On the Jakarta RESTful Web Services we have the client API that we can use to create our tests. To make this, you need to include on a maven stand-alone application the following dependencies to provide the resolution of the class types:

```xml
    <dependencies>
        <dependency>
            <groupId>jakarta.ws.rs</groupId>
            <artifactId>jakarta.ws.rs-api</artifactId>
            <version>4.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-client</artifactId>
            <version>4.0.0-M2</version>
        </dependency>
    </dependencies>
```

Then you can create the Class to call the services, check the following example:

```java
public class Main {
    
    public static void main(String[] args) {
        Main main = new Main();
        main.callGet();
        main.callPost();
    }
    
    public void callGet() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/myapplication/api/hello-world").request().get();
        System.out.println(response.readEntity(String.class));
    }
    
    public void callPost() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://localhost:8080/myapplication/api/hello-world")
                .request().post(Entity.entity("hello my name is Alfonso", MediaType.TEXT_PLAIN), Response.class);
        System.out.println(response.readEntity(String.class));
    }
}
```

As you can see from the example, the first thing we need is to create our jakarta.ws.rs.client.Client implementation. To do that, we need to use the class jakarta.ws.rs.client.ClientBuilder and call the method newClient() this will return an instance for the Client class. Then we need to provide the target URL that we want to consume using the method target() passing the URI of the RESTful web service. The target() method will return a jakarta.ws.rs.client.WebTarget instance, then we need to invoke que method request() from the WebTarget, this will return a jakarta.ws.rs.client.Invocation.Builder interface instance. For the GET service we need to call the get() method from the Builder interface, and the result will be wrapped using the jakarta.ws.rs.core.Response class. To read the result message, we call the method readEntity() from the Response instance adding the value of the returning result, in this case String class. 

For the POST service we need to call the post() method, for this method we need to indicate the jakarta.ws.rs.client.Entity and the Class to wrap the result of the service call. For the first parameter, we call the entity() method from Entity class, that needs two values, the content message, and the MediaType defined from the service to consume content MediaType.TEXT_PLAIN in this case. For the second parameter of the post method we will use the Response class as the wrapper to save the result of the service call. Finally, we read the result using the method readEntity() and adding the String class to get the message.

-----
#### **Task**
Now is the time to create your stand-alone client application. Copy most of the code content provided from the examples and test your Hello World Rest Service.

-----

#### Using Query and Path parameters

For RESTful Web Services we can pass parameters to determine the specific object we need from the data that is available from the database. This is very useful because we can define the criteria to access our resources to optimize results and simplify implementation. To indicate parameters we have two flavors that we can use independently or together. It depends on the use we want to provide to the resource. The two modes are: query and path parameters.

##### Query Parameters

To indicate query parameters, we need to use the annotation @QueryParam applied on the list of parameters from the method. The value will be provided from the request that accesses the resource, that means that the value is provided on the URL of the request.

For example, if we want to send the name property with a value, we need to indicate in the following way on our resource class:

```java
@Path("/hello-world")
public class HelloWorldResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@QueryParam("name") String name) {
        return "Hello World! " + name;
    }
}
```
To send the value on the request, we need to use something like the following:

```http request
curl http://localhost:8080/myapplication/api/hello-world?name=Alfonso
```

If we need to include multiple using different types, we can do it as follows:

```java
@Path("/hello-world")
public class HelloWorldResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@QueryParam("name") String name, @QueryParam("lastName") String lastName,
                             @QueryParam("age") int age) {
        return "Hello World!" + name + ", " + lastName + ", " + age;
    }
}
```
To send more than one value to the URL we need to separate with **&** symbol:

```http request
curl "http://localhost:8080/myapplication/api/hello-world?name=Alfonso&lastName=Valdez&age=40"
```

---
**NOTE**
The **&** on curl request is interpreted as a separation of commands or run a command in background it depends on the kind of shell.
To use correctly for our request enclose the entire URL with double quotes.

---

For the Jakarta REST Client API we can do the following to include query parameters:

```java
    public void callGet() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://localhost:8080/myapplication/api/hello-world")
                .queryParam("name", "Alfonso")
                .queryParam("lastName", "Valdez")
                .queryParam("age", 40)
                .request()
                .get();
        System.out.println(response.readEntity(String.class));
    }
```

As we can see, all we need to do to pass a parameter is to invoke the queryParam() method on the jakarta.ws.rs.client.WebTarget instance returned by the target() method invocation on our Client instance. The first argument to this method is the parameter name and must match the value of the @QueryParam annotation on the web service. The second parameter is the value that we need to pass to the web service


##### Path Parameters

In the case of Path parameters as the name suggests, these types of parameters should need to be part of the path to access the resource. The following example shows this case:

```java
@Path("/hello-world")
public class HelloWorldResource {

    @GET
    @Path("{name}/")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@PathParam("name") String name) {
        return "Hello World!" + name;
    }
}
```
To send the value on the request, we need to use something like the following:

```http request
curl http://localhost:8080/myapplication/api/hello-world/Alfonso/
```

As you can see from the example, it is necessary to use the annotation @PathParam on the list of parameters and the name should correspond to the one defined on the value indicated on the @Path annotation between braces. If we need to indicate multiple parameters, we can do as follows:

```java
    @GET
    @Path("/{name}/{lastName}/{age}/")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@PathParam("name") String name, @PathParam("lastName") String lastName,
                             @PathParam("age") int age) {
        return "Hello World!" + name + ", " + lastName + ", " + age;
    }
```

To send multiple values on the curl check the following example:

```http request
curl http://localhost:8080/myapplication/api/hello-world/Alfonso/Valdez/40
```

For the Jakarta REST Client API we can do the following to include path parameters:

```java
    public void callGet() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://localhost:8080/DominicanWorkshop-1.0-SNAPSHOT/api/hello-world/{name}/{lastName}/{age}")
                .resolveTemplate("name", "Alfonso")
                .resolveTemplate("lastName", "Valdez")
                .resolveTemplate("age", 40)
                .request()
                .get();
        System.out.println(response.readEntity(String.class));
    }
```

For the client URL we indicated the value of the URI including the path parameters as defined on the REST service. To include the value, we need to call the method resolveTemplate(). This requires the name and the value as parameters.

-----
#### **Task**

This is the last task for the module. Expose the functionality as a REST service to make CRUD operations for the Book catalog project. I can give you here some advice to achieve the goal:

- Make a GET REST endpoint to find a Book by Id
- Make a GET REST endpoint to find all Books available
- Make a POST REST endpoint to insert a Book
- Make a PUT REST endpoint to update a Book
- Make a DELETE REST endpoint to delete a Book indicating the ID

-----

# Module 3 conclusion

From this last module we learn the following:

- Concept of RESTful Web Services
- How to implement a RESTful Web Service using Jakarta 11 with Payara Server
- How to test RESTful Web Services using the REST client API




