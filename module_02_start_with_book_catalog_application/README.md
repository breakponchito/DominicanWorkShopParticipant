# Jakarta 11 with Payara 7 WorkShop

## Participant

### **Module 2: Start with book catalog application.**

#### Use of Jakarta Starter

Lest start to create our base application using the Jakarta EE starter. This is a tool provided by the community to 
configure and create your base application for different versions of Jakarta EE. In this case, we need to create a base 
application for our book catalog. Go to the following link and create the application using and artifact id related to a 
book catalog. Here is the link of the Jakarta Starter: [Jakarta Starter](https://start.jakarta.ee/)

![Jakarta EE Starter](img\jakartaEEStarter.png)

---
**NOTE**
At this moment only Open Liberty runtime is available for the starter configuration for Jakarta 11. For the Workshop we should need to select the following options:
- Jakarta EE version: Jakarta EE 11
- Jakarta EE profile: Core Profile
- Jakarta SE version: Java SE 21
- Runtime: None
- Docker support: No
- Group/Artifact: use a name related to a book catalog or book store
---

After that, you can decompress on any location of your local environment, here is an example of the structure of the folders:

![Jakarta Starter App](img\folderStarterApp.png)

You can open with your desired IDE. In my case, I opened with IntelliJ IDEA:

![Jakarta App with IDEA](img\appOpenedWithIDEA.png)

As you can see from the details of the application, you can identify the pom.xml file that indicates that this application is based on maven. This is why it is important to install the software requirements indicated in the first module.

#### Configuring pom.xml
To advance with our Workshop we need to provide the correct configuration of our pom.xml file. You can check the default pom file provided from the Jakarta Starter, here the example:

![Default pom configuration](img\pomFileStructure.png)

How we will build a web application, we need to update our pom configuration to indicate that we are going to use web profile.

-----
#### **Task**
Go to the pom file and edit to indicate the use of web profile instead of core profile for Jakarta 11: you can go to the following page to get more information about the available profiles: [Jakarta EE 11 spec](https://jakarta.ee/specifications/)

Also, another page where you can find a latest version of the web profile is the following: [Maven Jakarta Web Profile](https://mvnrepository.com/artifact/jakarta.platform/jakarta.jakartaee-web-api)

After upgrading the application build and see if any error is shown

-----

#### Model our entity

Now is the time to model our application. For this, we need to use JPA as the technology that can help us to integrate our model with
databases. For definition, it is good to know that JPA is our ORM technology that permits us to map classes to tables in a database following some rules to correctly make this. The following rules need to be followed:
- Annotate the class with the annotation @Entity
- Indicate a field as an id with the annotation @Id
- For the id field I recommend to annotate with the annotation @GeneratedValue with strategy GenerationType.AUTO that permits to auto generate id's, 
with this we don't need to worry about to control their generation with a sequence at the database level, this automatically generates the sequence for us.

-----
#### **Task**
Now is the time for a new task. In this task, your work will be to create a class based on the following model:

![Book Entity Model](img\bookModel.png)

save the entity class on a different package from the default provided from the Jakarta Starter application

-----

#### Add persistence configuration

To declare our persistence unit, we need to define the persistence.xml into the application and locate in the folder: src/main/resources/META-INF

I will provide this persisten.xml file for you to add to the application. You can find this on the src folder from this module with the same structure for the final location for your project created with the Jakarta Starter.

Look at the persistence unit and check that we are targeting for the datasource value the following name: jdbc/__default this is the default datasource used in Payara Server. We are going to use that to simplify the creation of tables.

Additionally to this, we need to fix all references from the persistence.xml file regarding org.eclipse.persistence.jpa package. Payara Server is using eclipselink as the provider for the JPA. That is why we need to include those dependencies on the pom.xml file. Here are the dependencies that must be included in the file:

```xml
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.jpa</artifactId>
            <version>5.0.0-B08</version>
        </dependency>
```


-----
#### **Task**
Add the dependency to your pom.xml and see if the issue for referencing the package org.eclipse.persistence.jpa is solved. Build the application and try to deploy the war created.

-----

For the other properties added on the persistence.xml this is an explanation of what is doing each of them:
tags:
- persistence-unit: with this tag we can indicate the name of the persistence unit and indicate if we need to manage transactions with mode JTA
- provider: indicating the provider implementation for JPA, for our case EclipseLink
- jta-data-source: indicate the name of the datasource to use with the mode JTA, in our case the default datasource from Payara Server.
properties
- eclipselink.ddl-generation: this property indicates the way to create tables, in our case, we will use the value drop-and-create-tables. For reference, check the following link: [Eclipse Link ddl generation](https://eclipse.dev/eclipselink/documentation/2.5/jpa/extensions/p_ddl_generation.htm)
- eclipselink.logging.level: to verify logs from eclipselink implementation, for our case INFO. For reference, check the following link: [Eclipse Link Logging Level](https://eclipse.dev/eclipselink/documentation/2.5/jpa/extensions/p_logging_level.htm)

#### Deploy a war application to Payara Server

To deploy an application, we have two ways that we can choose:
- Using the Admin console
- Using command line

##### Using the Admin console

To deploy using the Admin console, we must need to go to the following url: localhost:4848
This will open the Payara Admin console, here is an example:

![Payara Admin Console](img\payaraAdminConsole.png)

Then you need to go to the Applications section and select option deploy to select the war file that you want to deploy to the application server, here is an example:

![Admin Console Application Option](img\payaraApplicationsDeploy.png)

![Admin Console Application Deploy File](img\payaraApplicationDeployFile.png)

Then you click on the OK option to proceed with the deployment. Once deployed, you will see the following page: 

![Admin Console Deploy Finished](img\payaraDeployFinished.png)

Finally, click on the application and select View Endpoints to select the following endpoint that is provided from the default source from the Jakarta Starter application: /jakartaee-book-store/rest/hello or open with the full url: localhost:8080/jakartaee-book-store/rest/hello

![Payara Application Info](img\payaraApplicationDeployInfo.png)

![Payara Application View Endpoints](img\payaraApplicationViewEndpoints.png)

![Hello Endpoint](img\callingHelloEndpoint.png)

##### Using command line

With command line, you need to use the following command:

```console
asadmin deploy pathofyourwar/name.war
```

wait for a few seconds and then check logs from the terminal, this is an example:

![Deploy Command](img\deployCommand.png)

The message **Command deploy executed successfully** indicates that the application now is ready to use. Open browser tab and go to the following url: localhost:8080/jakartaee-book-store/rest/hello

![Hello World](img\helloWorldFromBrowser.png)

-----
#### **Task**
Deploy your application using one of the available methods. After that test if the endpoint for the hello world is available.

-----

### Define a service component to interact with the Entity
Now you have the model, and we want to execute some inserts to save our first rows on a table. Right now we don't have any endpoint or public service to add our data. The most simple way is to insert queries directly to the database. We can do that by connecting to our embedded database created on the sources of the Payara Server. First, we need to declare the entity manager on some component to permit the creation of the tables once deploying the application, then we can connect and insert data to our table.

I will provide the sql insert query that you can use to insert some rows to the table and verify if those rows were inserted on the table.

What we need to do is just to open the HelloWorldResource class and add the following lines of code before any other declaration of methods and redeploy the application:

```java
    @PersistenceContext
    private EntityManager em;
```

With this line, what we are going to achieve is the automatic creation of tables and sequence to the database. We can check that with our client DB, from IntelliJ IDEA or another tool like NetBeans. Here are some examples:

![IntelliJ IDEA Database Viewer](img\intelliJDatabaseViewer.png)

![NetBeans Database Viewer](img\netBeansDatabaseViewer.png)
-----
#### **Task**
Declare the injection of the PersistenceContext to the HelloWorldResource class, after that redeploy application and verify if the table and sequence are available in the database.

-----

Once the application is available now from a query console using IntelliJ IDEA or another tool. Connect to the database and execute the query provided for you. Here are some examples:

![Insert execution from IntelliJ IDEA](img\intelliJInsertExecution.png)

Verify the data added on the table

![Inserted rows](img\insertedRows.png)

-----
#### **Task**
Insert the rows from the insert.sql file provided for you. Use the tool you prefer to connect to the database and insert the data.

-----

### Unit test application

Something important to provide to our application is unit testing. For this, we need to include on our project additional dependencies to create an integration test for our entity. The dependencies needed for the application are the following:

```xml
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.11.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.11.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.2.224</version>
            <scope>test</scope>
        </dependency>
```

We are going to use JUnit 5 to create our integration test. To simplify this, I will share with you the unit test created to test the insertion of a row on an in memory table and a select to verify the row created.

Copy that file and set on the corresponding place for unit test files on your project. On my side it looks as follows:

![Structure of folder for Unit Test](img\structureOfFoldersTest.png)

Another thing that we need to edit is the persistence.xml file ti declare an addition persistence unit for the integration tests. Edit your file and add the following content:

```xml
    <persistence-unit name="integration-test" transaction-type="RESOURCE_LOCAL">
        <class>putyourpackagehere.Book</class>
        <properties>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:h2:mem:test:sample;DB_CLOSE_ON_EXIT=FALSE;" />
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver" />
            <property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create" />
            <property name="eclipselink.logging.level" value="FINE"/>
        </properties>
    </persistence-unit>
```
My final persistence.xml file looks as follows:

![Edit Persistence for Unit Testing](img\persistenceWithAdditionalPersistenceUnit.png)

---
**NOTE**
How we are adding more than one persistence unit on our configuration it is important to update all the places where the PersistenceContext is injected and update indicating the name of the persistence unit to use, for example: 

```java
    @PersistenceContext(unitName = "pu1")
    private EntityManager em;
```
If you don't upgrade this, you will get some exception while deploying again the application because the Server can't determine which unit test to use. That is why we need to indicate.

---

To run our test, you can execute using any of the available plugins for your IDE for maven or execute the following command from the terminal under the base folder of your application:

```console
mvn test
```
You will see and output like this:

![Result of test execution](img\resultOfTestExecution.png)

### Introduction to Records

Now it is time to talk about Java Records because we will define a Record to manage a subset of data from our first entity. The idea for this is to see the record as a DTO in cases when you don't need the full information from the entity.

##### What is a Record? 

A Record is a simplified way to create an immutable object and removes the need to define boilerplate code. A record provides default implementation for common methods inherited from Object like: toString(), hashCode() and equals(). To define a Record, we need to use the following structure:

```java
record NameOfRecord(TypeOfField nameOfField, TypeOfField2 nameOfField2, ...){}
```
The TypeOfField can be any available type used in Java, also you can use any of the modifiers for the class, like public, private or protected.

Remember that a Record is an inmutable object, that means that when you create, you can't change. That is why this is a candidate to be a wrapper for data that we read from the database, once we read and set on the object, then we can't change. For example, a use case can be to provide the data to the view to present in some way that information.

If you want to have more information about Records, please check the following link: [JAva Records](https://docs.oracle.com/en/java/javase/17/language/records.html)

Check out the current Entity for Book:

![Book Model](img\bookModel.png)

From this definition probably we can define a DTO to get a subset of the information for some purpose

### Specifications that support Records

Here is the list of other specifications that now support Records:

- Jakarta JPA
- Jakarta Validation
- Jakarta Expression Language

### Define a record to get data from our repository

-----
#### **Task**
Now we need a record to define a subset of information from our Book entity and add a Unit Test for it. Define a Record named BookDTO with the following fields: Long id,String title, String author, String description, String imageName, double price

Add the following code with a new Unit Test method to see if the data is returned as expected:

```java
    @Test
    void testInsertAndSelectRecord() throws Exception {
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

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDTO> cq = cb.createQuery(BookDTO.class);
        Root<Book> root = cq.from(Book.class);
        cq.select(cb.construct(BookDTO.class, root.get("id"), root.get("title"),
                root.get("author"), root.get("description"), root.get("imageName"), root.get("price")));
        List<BookDTO> list = entityManager.createQuery(cq).getResultList();
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).author(), "Craig Walls");
        assertEquals(list.get(0).title(), "Spring  AI in Action");
    }
```

Review the code and identify the lines that define the query structure using Criteria Builder. Then identify the lines where the mapping of the Record is defined.

-----

### Define and embeddable with Records

An Embeddable in JPA can help to abstract part of the information from a table on a new model for Java Classes. Now with Records, we can define the @Embedabble as a Record and then indicate the embeddable into the class with the @Embedded annotation. This is an example:

```java
 @Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int age;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "author")
    private Book book;
    
    //getters and setters
}

@Embeddable
public record Address(String street, String city, String state, String zip) {
}

```

-----
#### **Task**
Now we will define the new components to use Embeddable. For this, we need to create the following model:

![Model Complete](img\diagramModelComplete.png)

To reduce time, I provided the resources for this model on the sources of this module. Please copy the content of the files and move to your project.

To fix any issue for the unit test, I also shared the upgraded unit test here:

```java
@Test
void testSelect() throws Exception {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Book book = new Book();
    book.setTitle("Spring  AI in Action");
    book.setIsbn("978-0596152657");
    book.setDescription("Generative AI tools like ChatGPT cause an immediate jaw drop for almost everyone who encounters them.");
    book.setPublicationDate(LocalDate.of(2024, 8, 1));
    book.setPrice(35.00);
    book.setImageName("imageBook1.png");
    Author author = new Author();
    Address address = new Address("street", "city", "state", "zip");
    author.setName("Craig Walls");
    author.setBook(book);
    author.setAddress(address);
    book.setAuthor(author);
    entityManager.persist(book);
    entityManager.getTransaction().commit();
    List<Book> list = entityManager.createQuery("select b from Book b", Book.class).getResultList();
    assertEquals(list.size(), 1);
    assertEquals(list.get(0).getAuthor().getName(), author.getName() );
    assertEquals(list.get(0).getTitle(), "Spring  AI in Action");
}

@Test
void testInsertAndSelectRecord() throws Exception {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Book book = new Book();
    book.setTitle("Spring  AI in Action");
    book.setIsbn("978-0596152657");
    book.setDescription("Generative AI tools like ChatGPT cause an immediate jaw drop for almost everyone who encounters them.");
    book.setPublicationDate(LocalDate.of(2024, 8, 1));
    book.setPrice(35.00);
    book.setImageName("imageBook1.png");
    Author author = new Author();
    Address address = new Address("street", "city", "state", "zip");
    author.setName("Craig Walls");
    author.setBook(book);
    author.setAddress(address);
    book.setAuthor(author);
    entityManager.persist(book);

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<BookDTO> cq = cb.createQuery(BookDTO.class);
    Root<Book> root = cq.from(Book.class);
    cq.select(cb.construct(BookDTO.class, root.get("id"), root.get("title"),
            root.get("author"), root.get("description"), root.get("imageName"), root.get("price")));
    List<BookDTO> list = entityManager.createQuery(cq).getResultList();
    entityManager.getTransaction().commit();
    assertEquals(list.size(), 1);
    assertEquals(list.get(0).author().getName(), author.getName() );
    assertEquals(list.get(0).title(), "Spring  AI in Action");
}
```
Also, you need to upgrade your persistence unit for the integration test with the following:

```xml
    <persistence-unit name="integration-test" transaction-type="RESOURCE_LOCAL">
        <class>putyourpackagehere.Book</class>
        <class>putyourpackagehere.Author</class>
        <properties>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:h2:mem:test:sample;DB_CLOSE_ON_EXIT=FALSE;" />
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver" />
            <property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create" />
            <property name="eclipselink.logging.level" value="FINE"/>
        </properties>
    </persistence-unit>
```

After making all of those changes to your project, execute the following command:

```console
mvn test
```

or

```console
mvn clean install package
```

Review the results from the unit test. On my side, this is my result:

![Results for two unit tests](img\resultsTwoUnitTests.png)

-----

### Use Bean Validation for Records

As an upgrade for Bean Validation now we can apply Annotations validations for the records in the following way:

```java
public record Address(@NotNull(message = "street must not be null") String street,
                      @NotEmpty(message = "city must not be empty") String city,
                      @NotBlank(message = "state must not be blank") String state,
                      String zip) {
}
```

As you saw, it is very simple to include validations for our Records, and this is an update for Jakarta Bean Validation for Jakarta 11.

-----
#### **Task**
The following task is the end for this module, and now you need to add validations for the Record class generated for Address. 

Also, you need to include new dependencies to test bean validation, please add the following to your pom.xml:

```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>
<dependency>
    <groupId>org.glassfish.expressly</groupId>
    <artifactId>expressly</artifactId>
    <version>6.0.0</version>
</dependency>
```

On your unit test add the following declaration on top of your class:

```java
private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
```

Then include the following unit test to see if all is working as expected. You can edit the message as you need. In this unit test, I'm using the same messages defined previously to compare the violations.

```java
 @Test
void testInsertAndSelectRecordWithValidations() throws Exception {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Book book = new Book();
    book.setTitle("Spring  AI in Action");
    book.setIsbn("978-0596152657");
    book.setDescription("Generative AI tools like ChatGPT cause an immediate jaw drop for almost everyone who encounters them.");
    book.setPublicationDate(LocalDate.of(2024, 8, 1));
    book.setPrice(35.00);
    book.setImageName("imageBook1.png");
    Author author = new Author();
    Address address = new Address(null, "", " ", "zip");
    Set<ConstraintViolation<Address>> violations = validator.validate(address);
    author.setName("Craig Walls");
    author.setAddress(address);
    book.setAuthor(author);
    entityManager.persist(book);

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<BookDTO> cq = cb.createQuery(BookDTO.class);
    Root<Book> root = cq.from(Book.class);
    cq.select(cb.construct(BookDTO.class, root.get("id"), root.get("title"),
            root.get("author"), root.get("description"), root.get("imageName"), root.get("price")));
    List<BookDTO> list = entityManager.createQuery(cq).getResultList();
    entityManager.getTransaction().commit();
    assertEquals(list.size(), 1);
    assertEquals(list.get(0).author().getName(), author.getName());
    assertEquals(list.get(0).title(), "Spring  AI in Action");
    assertTrue(!violations.isEmpty());
    assertEquals(violations.size(), 3);

    Set<String> errorMessages = new HashSet<>();
    errorMessages.add("street must not be null");
    errorMessages.add("city must not be empty");
    errorMessages.add("state must not be blank");

    List<ConstraintViolation<Address>> resultList = violations.stream()
            .filter(v -> errorMessages.contains(v.getMessage())).toList();

    assertEquals(resultList.size(), 3);

}
```

Finally, run the application to review results. All the tests should need to pass

```console
mvn test
```

or

```console
mvn clean install package
```

![Results for three unit tests](img\resultsThreeUnitTests.png)

-----

# Module 2 conclusion

Until here we started our application to add the JPA models for the book catalog application. We saw some of the benefits to upgrade to Jakarta 11, and also we saw how it is simple to use with Payara Server. Here is a summary of the key points reviewed in this module:

- Jakarta JPA now support Records
- Jakarta Bean Validation annotations for Records
- Payara 7 simple deployment of applications
- Apply unit tests