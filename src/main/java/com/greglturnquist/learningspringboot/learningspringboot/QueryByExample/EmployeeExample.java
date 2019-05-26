<<<<<<< HEAD
package com.greglturnquist.learningspringboot.learningspringboot.QueryByExample;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
=======
package com.greglturnquist.learningspringboot.learningspringboot.Example;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

<<<<<<< HEAD
import java.util.UUID;

=======
>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

public class EmployeeExample {

    @Resource
    private EmployeeRepo employeeRepository;

<<<<<<< HEAD
    @Resource
    private MongoOperations mongoOperations;

    /**
     * Start by using dropCollection to clean things out
     * Next, create a new Employee, and insert it into MongoDB
     * Create a second Employee and insert it as well
     */
    public void preloadDatabase() {
        mongoOperations.dropCollection(Employee.class);

        Employee e1 = new Employee();
        e1.setId(UUID.randomUUID().toString());
        e1.setFirstName("Bilbo");
        e1.setLastName("Baggins");
        e1.setRole("burglar");

        mongoOperations.insert(e1);

        Employee e2 = new Employee();
        e2.setId(UUID.randomUUID().toString());
        e2.setFirstName("Frogo");
        e2.setLastName("Baggins");
        e2.setRole("ring bearer");

        mongoOperations.insert(e2);
    }


    public void teste() {
        Employee e = new Employee();
        e.setFirstName("Bilbo");
=======
    public void teste() {
        Employee e = new Employee();
        e.setFirstName("Bilbo");

>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766
        final Example<Employee> exampleEmployee = Example.of(e);

        final Mono<Employee> one = employeeRepository.findOne(exampleEmployee);
    }

<<<<<<< HEAD
    /**
     * We create another Employee probe
     * We deliberately set the lastName value as lowercase
     * Then we create a custom ExampleMatcher using matching()
     * withIgnoreCase says to ignore the case of the values being checked
     * withMatcher lets us indicate that a given document's lastName starts with the
     * probe's value
     * withIncludeNullValues will also match any entries that have nulled-out values
     * Finally, we create an Example using our probe, but with this custom matcher
     */
=======
>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766
    public void customMatcher() {
        Employee e = new Employee();
        e.setFirstName("baggins");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withMatcher("lastName", startsWith())
                .withIncludeNullValues();

        final Example<Employee> of = Example.of(e, matcher);

        Flux<Employee> multipleEmployees = employeeRepository.findAll(of);
    }

}
