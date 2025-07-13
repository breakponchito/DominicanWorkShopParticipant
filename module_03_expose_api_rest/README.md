# Jakarta 11 with Payara 7 WorkShop

## Participant

### **Module 3: Expose api rest.**

#### Introduction to Jakarta RESTful Web Service

Representational State Transfer (REST) is an architectural style where the web services are viewed as resources, and ca be identified by Uniform Resources Identifiers (URIs). The Web Services implemented using REST are named RESTful Web Services. With Jakarta EE the specification that defines this style is Jakarta RESTful Web Services. For Jakarta 11 now we are on the version 4.0.

For Jakarta RESTful Web Services 4.0 we have multiple implementations showed on the following table:

| Name           | version     |
|----------------|-------------|
| RESTEasy       | 7.0.0.Beta1 |
| Eclipse Jersey | 4.0.0-M2    |

---
**NOTE**
This list is not final because this spec was recently released for Jakarta 11 and other new implementations can be listed in feature. Payara Server is using Eclipse Jersey because is the reference implementation.

---

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

When developing our RESTful Web Services, it is important to access the component. To make that, we are going to use the annotation ***@Path***. With this annotation, we are marking a class to be identified as a resource and therefore be accesible using URI's. After this, we need to indicate which methods this resource will provide using other annotations to handle it. The following table shows each of those annotations to use:

| Annotation | Method                                                                                                                            |
|------------|-----------------------------------------------------------------------------------------------------------------------------------|
| @GET       | Used to decorate a method to indicate that this method will retrieve an existing resource.                                        |
| @POST      | Used to decorate a method to indicate that this method will create a new resource.                                                |
| @PUT       | Used to decorate a method to indicate that this method will update an existing resource or create if the resource doesn't exists. |
| @DELETE    | Used to decorate a method to indicate that this method will delete an existing resource.                                          |
| @HEAD      | Used to decorate a method to indicate that this method will return an HTTP header with no body.                                   |
| @PATCH     | Used to decorate a method to indicate that this method will modify partially a resource.                                          |
| @OPTIONS   | Used to decorate a method to indicate that this method will retrieve a communication options available for a resource.            |
