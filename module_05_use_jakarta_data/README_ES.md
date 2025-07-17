# Jakarta 11 with Payara 7 WorkShop

## Participante

### **Módulo 5: Usar Jakarta Data para interactuar con entidades.**

#### ¿Qué es Jakarta Data?

Para empezar, necesitamos definir qué es Jakarta Data para entender por qué ahora forma parte de la especificación Jakarta 11. El objetivo principal de Jakarta Data es proporcionar una interfaz sencilla para interactuar con los datos, introduciendo el patrón **Repository**. Esta nueva API simplifica el acceso y la gestión de la base de datos y proporciona generación automática de consultas basada en una combinación de nuevas anotaciones. Algunas de las ventajas de usar esta nueva especificación son las siguientes:

- Elimina la complejidad al interactuar con la persistencia de datos.
- Introduce un mecanismo de paginación para los conjuntos de resultados.
- El framework proporciona generación automática de consultas basada en abstracciones.
- Reduce el código repetitivo necesario para implementar la capa de acceso a datos.
- Utiliza anotaciones para abstraer la funcionalidad.

![Repository Pattern](img/repositoryPattern.png)

Jakarta Data separa la persistencia y el modelo con la interfaz del repositorio. Los repositorios son las clases que encapsulan la lógica de acceso a datos, desacoplando así el mecanismo de persistencia del modelo de dominio. Este repositorio actúa como una puerta de enlace para acceder a datos persistentes de uno o más tipos de entidades.

Si requieres mas detalles sobre lo que es el patron Repository ve al siguiente link: [Repository Pattern](https://deviq.com/design-patterns/repository-pattern)

#### Integrar Jakarta Data

Para empezar con Jakarta Data, lo primero que necesitamos tener es la definición de una entidad, por ejemplo, la siguiente:

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

Luego, necesitamos proporcionar nuestra implementación de la interfaz del Repositorio, como la siguiente:

```java
@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Long> {
}
```

Este es un ejemplo de cómo usar la interfaz `CrudRepository` proporcionada, que (como su nombre sugiere) ofrecerá operaciones CRUD para la entidad especificada en el tipo.

Finalmente, para usarlo, necesitamos inyectar esta interfaz en el componente que requiera usarla. Por ejemplo, un *endpoint* REST; aquí tienes el ejemplo de código para esto:

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

Otras interfaces proporcionadas por la API que podemos reutilizar son las siguientes:

- `DataRepository<T, K>`: Esta interfaz se encuentra en la cima de la jerarquía y no define ningún método.
- `BasicRepository<T, K>`: Proporciona las operaciones más comunes para las entidades, como guardar, eliminar y buscar por ID.
- `CrudRepository<T, K>`: Esta interfaz extiende `BasicRepository` y proporciona métodos para realizar las operaciones CRUD para la entidad, como insertar, actualizar, eliminar y buscar.

No es obligatorio usar ninguna de las interfaces anteriores, ya que puedes definir los métodos para realizar toda la funcionalidad combinando las diferentes anotaciones de la API. Un ejemplo de una implementación sin esas interfaces sería la siguiente:

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

#### **Tarea**

Ahora tu tarea es implementar un patrón de repositorio usando Jakarta Data para una entidad de nuestro proyecto. Elige el patrón CRUD y selecciona la forma de hacerlo. Al final, proporcionarás un *endpoint* REST para realizar las operaciones CRUD.

-----

---
**NOTE**
Para que este módulo ejecute la aplicación, es necesario comentar la segunda unidad de persistencia declarada en el archivo persistence.xml y deshabilitar la prueba unitaria que se agregó en un módulo previo. Esto evitará problemas al intentar ejecutar la aplicación.

---

