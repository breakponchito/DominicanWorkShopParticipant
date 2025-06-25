# Jakarta 11 with Payara 7 WorkShop

## Participant

### **Module 5: Use Jakarta Data to interact with entities.**

#### What is Jakarta Data?

To start, we need to define what is Jakarta Data to understand why this is now part of the Jakarta 11 specification. The aim goal for Jakarta Data is to provide a simple interface to interact with Data, by introducing the Repository pattern. This new api simplifies the access and management of the database and provides automatic query generation based on a combination of new annotations. Some of the advantages of using this new spec are the following:

- Removes complexity to interact with the data persistence.
- Introduction to a pagination mechanism for the result datasets.
- The framework provides automatic query generation based on abstractions.
- Reduced boilerplate code required to implement data access layer.
- Use of annotations to abstract functionality

![Repository Pattern](img/repositoryPattern.png)

Jakarta Data separates persistence and the model with the repository interface. The repositories are the classes that encapsulate the data access logic, thus decoupling the persistence mechanism from the domain model. This repository acts as a gateway for accessing persistent data of one or more entity types.


#### Integrate Jakarta Data 

To start with Jakarta Data, the first thing we need to have is the definition of an entity, for example, the following:

```java
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

Then we need to provide our Repository interface implementation, like the following:

```java
@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Long> {
}
```
This is an example of using the provided CrudRepository interface that will provide (as the same suggest) CRUD operations for the entity specified on the type.

Finally, to use, we need to inject this interface on the component that requires to use it. For example, a rest endpoint, here is the code example for this:

```java
@Path("/employee")
public class EmployeeResource {

    @Inject
    private EmployeeRepository employeeRepository;

    @GET
    @Produces("application/json")
    public List<Employee> findAll() {
        return employeeRepository.findAll().toList();
    }
}
```

Other interfaces provided by the API that we can reuse are the following:

- DataRepository<T, K>: This interface is at the top of the hierarchy and does not define any method.
- BasicRepository<T, K>: This provides the most common operations for entities, like save, delete and findById.
- CrudRepository<T, K>: This interface extends BasicRepository and provides methods to make the CRUD operations for the entity like insert, update, delete and find.

It is not mandatory to use any of the previous interfaces as you can define the methods to make all the functionality by combining the different annotations from the API. An example of an implementation without those interfaces will be like the following:

```java
@Repository
public interface EmployeeRepository {
    
    @Find
    public Employee findById(int id);
    
    @Delete
    public void deleteById(int id);
    
    @Save
    public void save(Employee employee);
    
    @Update
    public void update(Employee employee);
    
    @Query("From Employee where name = :name Order By name asc")
    public Employee findByName(@Param("name") String name);
    
}
```

-----
#### **Task**

Now your task is to implement a repository pattern using Jakarta Data for an entity from our project. Choose the CRUD pattern and select the way to do. At the need you will provide a rest endpoint to make the CRUD operations.

-----
