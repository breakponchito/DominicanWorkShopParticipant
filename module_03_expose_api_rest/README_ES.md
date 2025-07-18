# Jakarta 11 with Payara 7 WorkShop

## Participante

### **Módulo 3: Exponer API REST.**

#### Introducción a Jakarta RESTful Web Services

Representational State Transfer (REST) es un estilo arquitectónico donde los servicios web son vistos como recursos y pueden ser identificados por Uniform Resource Identifiers (URIs). Los servicios web implementados usando REST se denominan RESTful Web Services. Con Jakarta EE, la especificación que define este estilo es Jakarta RESTful Web Services. Para Jakarta 11, estamos en la versión 4.0.

Para Jakarta RESTful Web Services 4.0, tenemos múltiples implementaciones mostradas en la siguiente tabla:

| Nombre           | Versión     |
|----------------|-------------|
| RESTEasy       | 7.0.0.Beta1 |
| Eclipse Jersey | 4.0.0-M2    |

---
**NOTA:**

Esta lista no es definitiva porque esta especificación fue lanzada recientemente para Jakarta 11 y otras nuevas implementaciones pueden ser listadas en el futuro. Payara Server está usando Eclipse Jersey porque es la implementación de referencia.

---

El estilo arquitectónico REST está diseñado para usar un protocolo de comunicación sin estado, típicamente HTTP. Los siguientes principios fomentan que las aplicaciones RESTful sean simples, ligeras y rápidas:

- **Identificación de recursos a través de URI**: Un servicio web RESTful expone un conjunto de recursos que identifican los objetivos de la interacción con sus clientes. Los recursos se identifican mediante URIs, que proporcionan un espacio de direcciones global para el descubrimiento de recursos y servicios.
- **Interfaz uniforme**: Los recursos se manipulan utilizando un conjunto fijo de operaciones descritas por el protocolo. Consulte la tabla en el siguiente tema para ver la lista completa de métodos disponibles.
- **Mensajes autodescriptivos**: Los recursos están desacoplados de su representación para que su contenido pueda ser accedido en una variedad de formatos, como HTML, XML, texto plano, PDF, JPEG, JSON y otros formatos de documentos.
- **Interacciones con estado a través de enlaces**: Cada interacción con un recurso es sin estado; es decir, los mensajes de solicitud son autocontenidos. Las interacciones con estado se basan en el concepto de transferencia explícita de estado.

Para entender los servicios web RESTful, necesitamos introducir la semántica definida para usarlos. Las operaciones base que podemos realizar con los servicios web RESTful se definen en la siguiente tabla:

| Método  | Descripción                                                                                                    |
|---------|----------------------------------------------------------------------------------------------------------------|
| GET     | Una solicitud ***GET*** se utiliza para recuperar un recurso existente.                                                  |
| POST    | Una solicitud ***POST*** se utiliza para crear un nuevo recurso.                                                         |
| PUT     | Una solicitud ***PUT*** se utiliza para actualizar un recurso existente. Si el recurso no existe, puede ser creado. |
| DELETE  | Una solicitud ***DELETE*** se utiliza para eliminar un recurso.                                                           |
| HEAD    | Una solicitud ***HEAD*** devuelve un encabezado HTTP sin cuerpo.                                                      |
| PATCH   | Una solicitud ***PATCH*** se utiliza para la modificación parcial de un recurso.                                               |
| OPTIONS | Una solicitud ***OPTIONS*** recupera las opciones de comunicación disponibles para un recurso.                            |

Otra parte importante es el tipo de contenido gestionado por este tipo de servicio. Comúnmente, este tipo de servicio se asocia con JSON (JavaScript Object Notation), pero podemos cambiarlo usando algo llamado tipos MIME. MIME significa Multipurpose Internet Mail Extensions; se utiliza para indicar el tipo de datos que debe producir o consumir el servicio. Veremos cómo usarlo más adelante con ejemplos.

#### Desarrollando un servicio web RESTful simple de Jakarta

Al desarrollar nuestros servicios web RESTful, es importante acceder al componente. Para ello, vamos a usar la anotación ***@Path***. Con esta anotación, marcamos una clase o método para ser identificado como un recurso y, por lo tanto, ser accesible usando URIs. Después de esto, necesitamos indicar qué métodos proporcionará este recurso usando otras anotaciones para manejarlo. La siguiente tabla muestra cada una de esas anotaciones a usar:

| Anotación | Método                                                                                                                            |
|------------|-----------------------------------------------------------------------------------------------------------------------------------|
| @GET       | Se utiliza para decorar un método para indicar que este método recuperará un recurso existente.                                        |
| @POST      | Se utiliza para decorar un método para indicar que este método creará un nuevo recurso.                                                |
| @PUT       | Se utiliza para decorar un método para indicar que este método actualizará un recurso existente o lo creará si el recurso no existe. |
| @DELETE    | Se utiliza para decorar un método para indicar que este método eliminará un recurso existente.                                          |
| @HEAD      | Se utiliza para decorar un método para indicar que este método devolverá un encabezado HTTP sin cuerpo.                                   |
| @PATCH     | Se utiliza para decorar un método para indicar que este método modificará parcialmente un recurso.                                          |
| @OPTIONS   | Se utiliza para decorar un método para indicar que este método recuperará las opciones de comunicación disponibles para un recurso.            |

Otra parte importante para el servicio es indicar qué tipo de contenido producirá y consumirá. Para indicar esto, tenemos otras dos anotaciones que pueden ayudarnos. Las siguientes anotaciones ***@Produces*** y ***@Consumes*** nos ayudarán a indicar qué tipo de contenido vamos a usar. Para ambas anotaciones, necesitamos indicar el tipo de contenido, y podemos usar la clase `MediaType` para indicar usando algunas constantes definidas en esta clase que representan el tipo específico a ser gestionado por el servicio. La siguiente tabla muestra los más comunes, pero puedes consultar la definición completa aquí: [MediaType API](https://jakarta.ee/specifications/restful-ws/4.0/apidocs/jakarta.ws.rs/jakarta/ws/rs/core/mediatype)

| MediaType                  | Definición                                              |
|----------------------------|---------------------------------------------------------|
| MediaType.TEXT_PLAIN       | este valor representa el tipo de medio "text/plain"       |
| MediaType.TEXT_HTML        | este valor representa el tipo de medio "text/html"        |
| MediaType.APPLICATION_JSON | este valor representa el tipo de medio "application/json" |
| MediaType.APPLICATION_XML  | este valor representa el tipo de medio "application/xml"  |

Aquí hay un ejemplo de código para mostrar un servicio simple que devuelve y consume texto simple:

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
````

Para llamar a este servicio, necesitamos proporcionar la URL completa de la aplicación. Supongamos que desplegamos nuestra aplicación con el contexto raíz **myapplication** y que definimos nuestra ruta de recursos REST para nuestra aplicación como **api**, entonces necesitamos incluir el protocolo, el servidor y el puerto para acceder al servicio. El siguiente será el endpoint para acceder:

```http request
http://localhost:8080/myapplication/api/
```

Podemos usar cURL para consumir el servicio, el navegador u otra herramienta como [Postman](https://www.postman.com/) para probarlo.

Como ejemplo, si elijo usar cURL para probarlo, la siguiente será la llamada para cada uno de los endpoints:

Para acceder, método GET

```http request
curl http://localhost:8080/myapplication/api/hello-world
```

Para acceder, método POST

```http request
curl -X POST -d "mi nombre es Alfonso" -H "Content-Type: text/plain" http://localhost:8080/myapplication/api/hello-world
```

#### Configurando la ruta de recursos REST para nuestra aplicación

En el ejemplo anterior, dijimos que para nuestro servicio asumimos nuestra ruta de recursos REST como **api**; para configurar esto, necesitamos proporcionar una clase que extienda `jakarta.ws.rs.core.Application` y debe estar anotada con `@ApplicationPath`, y para la anotación necesitamos indicar el valor de la ruta de recursos REST deseada. El siguiente ejemplo muestra el caso:

```java
@ApplicationPath("/api")
public class BookStoreApplication extends Application {

}
```

Este componente de configuración no necesita especificar ningún método y puede ubicarse en cualquier paquete y definirse solo una vez.

-----

#### **Tarea**

## Ahora es el momento de experimentar. A partir de la aplicación base que estamos implementando, cree el primer servicio web RESTful proporcionando todos los componentes necesarios para que funcione. Recuerda que, por defecto, Jakarta Starter crea un recurso y una configuración para un servicio REST. Revisa esos componentes y edítalos según sea necesario. Copia también el ejemplo que mostramos aquí para realizar una llamada GET y POST sencilla.

#### Definiendo la aplicación cliente

Probamos el primer endpoint usando el navegador para métodos GET y cURL para POST. En Jakarta RESTful Web Services, tenemos la API cliente que podemos usar para crear nuestras pruebas. Para hacer esto, debe incluir las siguientes dependencias en una aplicación Maven independiente para proporcionar la resolución de los tipos de clase:

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

Luego puede crear la Clase para llamar a los servicios, consulte el siguiente ejemplo:

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
                .request().post(Entity.entity("hola mi nombre es Alfonso", MediaType.TEXT_PLAIN), Response.class);
        System.out.println(response.readEntity(String.class));
    }
}
```

Como puede ver en el ejemplo, lo primero que necesitamos es crear nuestra implementación de `jakarta.ws.rs.client.Client`. Para hacer eso, necesitamos usar la clase `jakarta.ws.rs.client.ClientBuilder` y llamar al método `newClient()`; esto devolverá una instancia de la clase `Client`. Luego, necesitamos proporcionar la URL de destino que queremos consumir usando el método `target()` y pasando el URI del servicio web RESTful. El método `target()` devolverá una instancia de `jakarta.ws.rs.client.WebTarget`, luego necesitamos invocar el método `request()` desde `WebTarget`; esto devolverá una instancia de la interfaz `jakarta.ws.rs.client.Invocation.Builder`. Para el servicio GET, necesitamos llamar al método `get()` desde la interfaz `Builder`, y el resultado se envolverá usando la clase `jakarta.ws.rs.core.Response`. Para leer el mensaje de resultado, llamamos al método `readEntity()` de la instancia `Response` agregando el valor del resultado de retorno, en este caso la clase `String`.

Para el servicio POST, necesitamos llamar al método `post()`; para este método, necesitamos indicar el `jakarta.ws.rs.client.Entity` y la Clase para envolver el resultado de la llamada al servicio. Para el primer parámetro, llamamos al método `entity()` de la clase `Entity`, que necesita dos valores: el mensaje de contenido y el `MediaType` definido desde el servicio para consumir contenido `MediaType.TEXT_PLAIN` en este caso. Para el segundo parámetro del método `post`, usaremos la clase `Response` como envoltorio para guardar el resultado de la llamada al servicio. Finalmente, leemos el resultado usando el método `readEntity()` y agregando la clase `String` para obtener el mensaje.

-----

#### **Tarea**

Ahora es el momento de crear su aplicación cliente independiente. Copie la mayor parte del contenido de código proporcionado en los ejemplos y pruebe su servicio REST "Hello World".

-----

#### Usando parámetros de tipo Query y Path

Para los servicios web RESTful, podemos pasar parámetros para determinar el objeto específico que necesitamos de los datos disponibles en la base de datos. Esto es muy útil porque podemos definir los criterios para acceder a nuestros recursos para optimizar los resultados y simplificar la implementación. Para indicar parámetros, tenemos dos opciones que podemos usar de forma independiente o conjunta. Depende del uso que queramos darle al recurso. Los dos modos son: parámetros de tipo query y path.

##### Parámetros de tipo Query

Para indicar parámetros de tipo query, necesitamos usar la anotación `@QueryParam` aplicada a la lista de parámetros del método. El valor se proporcionará desde la solicitud que accede al recurso, lo que significa que el valor se proporciona en la URL de la solicitud.

Por ejemplo, si queremos enviar la propiedad `name` con un valor, necesitamos indicarlo de la siguiente manera en nuestra clase de recurso:

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

Para enviar el valor en la solicitud, necesitamos usar algo como lo siguiente:

```http request
curl http://localhost:8080/myapplication/api/hello-world?name=Alfonso
```

Si necesitamos incluir múltiples tipos, podemos hacerlo:

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

Para enviar más de un valor a la URL, necesitamos separarlos con el símbolo **&**:

```http request
curl "http://localhost:8080/myapplication/api/hello-world?name=Alfonso&lastName=Valdez&age=40"
```

-----
**NOTA:**

El **&** en una solicitud cURL se interpreta como una separación de comandos o para ejecutar un comando en segundo plano, depende del tipo de shell.
Para usarlo correctamente para nuestra solicitud, encierre la URL completa entre comillas dobles.

-----

Para la API cliente de Jakarta REST, podemos hacer lo siguiente para incluir parámetros de consulta:

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

Como podemos ver, todo lo que necesitamos hacer para pasar un parámetro es invocar el método `queryParam()` en la instancia `jakarta.ws.rs.client.WebTarget` devuelta por la invocación del método `target()` en nuestra instancia de `Client`. El primer argumento de este método es el nombre del parámetro y debe coincidir con el valor de la anotación `@QueryParam` en el servicio web. El segundo parámetro es el valor que necesitamos pasar al servicio web.

##### Parámetros de tipo Path

En el caso de los parámetros de tipo path, como su nombre lo indica, este tipo de parámetros deben formar parte de la ruta para acceder al recurso. El siguiente ejemplo muestra este caso:

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

Para enviar el valor en la solicitud, necesitamos usar algo como lo siguiente:

```http request
curl http://localhost:8080/myapplication/api/hello-world/Alfonso/
```

Como puede ver en el ejemplo, es necesario usar la anotación `@PathParam` en la lista de parámetros y el nombre debe corresponder al definido en el valor indicado en la anotación `@Path` entre llaves. Si necesitamos indicar múltiples parámetros, podemos hacerlo de la siguiente manera:

```java
    @GET
    @Path("/{name}/{lastName}/{age}/")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@PathParam("name") String name, @PathParam("lastName") String lastName,
                             @PathParam("age") int age) {
        return "Hello World!" + name + ", " + lastName + ", " + age;
    }
```

Para enviar múltiples valores con cURL, consulte el siguiente ejemplo:

```http request
curl http://localhost:8080/myapplication/api/hello-world/Alfonso/Valdez/40
```

Para la API cliente de Jakarta REST, podemos hacer lo siguiente para incluir parámetros de ruta:

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

Para la URL del cliente, indicamos el valor del URI incluyendo los parámetros de ruta tal como se definen en el servicio REST. Para incluir el valor, necesitamos llamar al método `resolveTemplate()`. Esto requiere el nombre y el valor como parámetros.

-----

#### **Tarea**

Esta es la última tarea del módulo. Exponga la funcionalidad como un servicio REST para realizar operaciones CRUD en el proyecto del catálogo de libros. Aquí puedo darle algunos consejos para lograr el objetivo:

- Cree un endpoint REST GET para buscar un libro por ID.
- Cree un endpoint REST GET para buscar todos los libros disponibles.
- Cree un endpoint REST POST para insertar un libro.
- Cree un endpoint REST PUT para actualizar un libro.
- Cree un endpoint REST DELETE para eliminar un libro indicando el ID.

-----

# Conclusión del Módulo 3

Esto es lo aprendido al término del módulo:

- El concepto de arquitectura de servicios RESTful.
- Como implementar servicios RESTful utilizando Jakarta 11 con Payara Server.
- Como probar los servicios utilizando la API de cliente de REST.