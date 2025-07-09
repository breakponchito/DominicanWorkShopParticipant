# Jakarta 11 with Payara 7 WorkShop

## Participant

### **Module 6: Concurrency.**

#### Jakarta Concurrency

Jakarta Concurrency specification is part of the umbrella for the Jakarta 11 specification, and the principal focus is the development of a standard framework to managing and using concurrent programming within Enterprise Applications. This specification takes care of the challenges of multithreading in a managed environment, ensuring the concurrent operations don't compromise the integrity of the application server or container.

From that, it is not recommended to use native thread constructs because that will compromise the managed environment. That means that we can't use Java SE concurrency APIs such as java.lang.Thread or java.util.Timer directly.

The base implementation of this specification provides a managed version of the known interfaces from java.util.concurrent.ExecutorService. For that we have the following:

![Jakarta Concurrency Services](img/concurrencyServices.png)

- ManagedExecutorService: Similar to java.util.concurrent.ExecutorService, it's used for submitting asynchronous tasks for execution in a managed thread pool. It's ideal for offloading long-running operations from the main request thread.
- ManagedScheduledExecutorService: Extends ManagedExecutorService for scheduling tasks to run at a specific time or repeatedly.
- ContextService: Facilitates the capture and propagation of contextual information (like security context, classloader context, CDI context) across different threads. This is vital to avoid issues when a task is executed on a different thread than the one that initiated it.
- ManagedThreadFactory: Allows the creation of threads that inherit the context of the component that created them, ensuring proper security and other contexts are propagated.

#### Benefits to use the spec

By adding the implementation of this, we are gaining a lot of benefits, some of them are the following:

- Reduced boilerplate code and complexity: With this layer of managed services now is straightforward to use and implement on our daily basis, inject the resources when you need and use on your specific components.
- Compatibility with the Java SE Concurrency: As we said this spec is on top of the Java SE implementation providing the most recent additions for Concurrence, we will see how to use Virtual Threads more later.
- Improved performance and Responsiveness: By offloading long-running tasks to background threads, applications can remain responsive to user interactions.
- Better resource utilization: Efficiently uses available CPU cores by executing multiple tasks concurrently.
- Container Integrity: Ensures that concurrent operations respect the container's environment and don't lead to resource leaks or security vulnerabilities.
- Standardization: Provides a standard, vendor-neutral way to implement concurrency in Jakarta EE applications, promoting portability.
- Support for Modern Java Features: Newer versions of Jakarta Concurrency (like 3.1 in Jakarta EE 11) are incorporating support for modern Java features like Virtual Threads, further enhancing scalability and performance.

#### How to use it?

Payara provides default resources and the ability to define new depending on the needs to review the available services for concurrency go to the following URL: [Payara Home](http://localhost:4848/common/index.jsf)

![Payara Home](img/payaraHome.png)

Go to the Resources section on the left side menu and select **Concurrent Resources**, expand the option and see the available options:

![Concurrency Resources](img/concurrencyResources.png)

Now it is time to create a simple implementation using our concurrency managed services.

#### Use default resources

Let's start with the ManagedExecutorService. This will help us to execute tasks asynchronously, and the context of the container is propagated to the thread executing the task. To use, you need to inject the resource on your component like this:

```java
    @Resource
    private ManagedExecutorService managedExecutorService;
```

---
**NOTE**
For Concurrency 3.1 now you can use the annotation @Inject to get the resources
---

then we can use that executor to send the work to new threads, look at the following example:

```java
    @Inject
    private ManagedExecutorService managedExecutorService;

    @GET
    @Path("managedExecutorService")
    @Produces(MediaType.TEXT_PLAIN)
    public String getManagedExecutorService() throws ExecutionException, InterruptedException {
        AtomicInteger numberExecution1 = new AtomicInteger(0);
        AtomicInteger numberExecution2 = new AtomicInteger(0);
        Future future1 = managedExecutorService.submit(() -> {
            numberExecution1.incrementAndGet();
            System.out.println("Job running" + Thread.currentThread().getName());
        });
        
        Future future2 = managedExecutorService.submit(() -> {
            numberExecution2.incrementAndGet();
            System.out.println("Job running" + Thread.currentThread().getName());
        });
        
        future1.get();
        future2.get();
        System.out.println("Finishing jobs:" + (numberExecution1.get() +"  "+ numberExecution2.get()));
        return "Completed";
    }

```
In this example, we are creating two jobs on different threads using the ManagedExecutorService to get a new atomic number to be printed on the output.

Now we need to review the ContextService, this will help us to capture and propagate contextual information (e.g, security context, transaction context) from the thread where a task is submitted to the thread where it's executed.

In the following example, I will show you how the thread used from the ManagedExecutorService is using the contextual information
to print the JNDI resource name requested. This is automatically provided by the default ContextService provided by the server.

```java 
    @GET 
    @Path("contextService")
    @Produces(MediaType.TEXT_PLAIN)
    public String getContextService() throws ExecutionException, InterruptedException {
        Future<String> future1 = managedExecutorService.submit(() -> {
            try {
                return "getting data from context"+new InitialContext().lookup("java:comp/DefaultDataSource");
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        });
        
        String result = future1.get();
        
        return "Completed with result " + result ;
    }
```
The Next component is the ManagedThreadFactory. This will provide the ability to create threads managed by the container. Also, the context of the container is propagated to the thread executing the task. To use, you need to inject the resource on your component like this:

```java
    @Inject 
    private ManagedThreadFactory managedThreadFactory;
```
Then we can use the ManagedThreadFactory to execute a task:

```java
    @GET
    @Path("threadFactory")
    @Produces(MediaType.TEXT_PLAIN)
    public String processWithThreadFactory() throws ExecutionException, InterruptedException {
        Thread t  = managedThreadFactory.newThread(() -> {
            System.out.println("ManagedThread executing");
        });
        
        t.start();
        return "Completed";
    }
```

Finally, the ManagedScheduledExecutorService. This will help us to define a task to be executed at specified and periodic times. To use, you can Inject the default resource from the server:

```java
    @Inject
    private ManagedScheduledExecutorService managedScheduledExecutorService;
```

Then we can use the ManagedScheduleExecutorService. The first example show how to send a task at a specified time, after a delay:

```java
    @GET
    @Path("/scheduleTaskForGivenDelay")
    @Produces(MediaType.TEXT_PLAIN)
    public String processWithScheduledExecutorService() throws ExecutionException, InterruptedException {
        managedScheduledExecutorService.schedule(() -> System.out.println("ScheduledExecutor executing"), 10, TimeUnit.SECONDS);
        return "Scheduled task";
    }
    
    @GET
    @Path("/scheduleTaskForPeriodicTime")
    @Produces(MediaType.TEXT_PLAIN)
    public String scheduleServiceAllTheTime() throws ExecutionException, InterruptedException {
        managedScheduledExecutorService.scheduleAtFixedRate(() -> System.out.println("ScheduledExecutor with rate"), 3, 1, TimeUnit.SECONDS);
        managedScheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("ScheduledExecutor with delay"), 2, 2, TimeUnit.SECONDS);
        return "Scheduled task";
    }
```

The previous two examples show how you can create a task to start at a specified time, and the second endpoint shows how you can execute a task for a periodic time. The method scheduleAtFixedRate submits a periodic action that becomes enabled first after the given initial delay, and subsequently with the given period; that is, executions will commence after initialDelay, then initialDelay + period, then initialDelay + 2 * period, and so on. The method scheduleWithFixedDelay submits a periodic action that becomes enabled first after the given initial delay, and subsequently with the given delay between the termination of one execution and the commencement of the next.

Other implementations that we need to mention are the ForkAnJoinPool and the CronTrigger configuration. First, start with CronTrigger as the name suggests this is used to configure the cron notation on a class to be used as a configuration to execute periodic tasks. With the following example, you will see how to use it if you want to see the reference of the API to understand the details for the cron notation review the following page: [CronTrigger API](https://jakarta.ee/specifications/concurrency/3.1/apidocs/jakarta.concurrency/jakarta/enterprise/concurrent/crontrigger)

To use this CronTrigger configuration you need to combine with the ManagedScheduleExecutorService, here the example:

```java
    @Inject
    ManagedScheduledExecutorService managedScheduledExecutorService;
```

Then you can use the Trigger interface to save the reference of the CronTrigger object to be used with the ManagedScheduleExecutorService on the method schedule. Here the example:

```java
    @GET
    @Path("cronTrigger")
    @Produces(MediaType.TEXT_PLAIN)
    public String getText() throws InterruptedException {
        AtomicInteger numberExecution = new AtomicInteger();
        ZoneId santDomingo = ZoneId.of("America/America/Santo_Domingo");
        Trigger trigger = new CronTrigger("* * * * * *", santDomingo);
        ScheduledFuture feature = managedScheduledExecutorService.schedule(() -> {
            numberExecution.getAndIncrement();
            System.out.println("Cron Trigger running");
        }, trigger);
        Thread.sleep(10000);
        feature.cancel(true);
        return "CronTrigger Submitted:"+numberExecution.get();
    }
```

From the previous example, we can see that we configured a CronTrigger to be executed every second and the task will use the trigger as the configuration. The task will increment a number and will print a message. Then we controlled the execution with a delay of 10,000 milliseconds to permit the task work and print and increment the number 10 times. When finished, we can get the number incremented with value of 10.


-----
#### **Task**

With all of this information now is your turn to experiment. That is why your job for now is to copy each of the examples described here and use in your own implementation. Everything is permitted you need to experiment.

-----




#### Define your custom resource

#### What is Virtual Threads?

#### How to use with Jakarta 11?


