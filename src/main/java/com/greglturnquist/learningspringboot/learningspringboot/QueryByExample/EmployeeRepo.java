<<<<<<< HEAD
package com.greglturnquist.learningspringboot.learningspringboot.QueryByExample;
=======
package com.greglturnquist.learningspringboot.learningspringboot.Example;
>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

<<<<<<< HEAD
/**
 * It's an interface declaration, meaning, we don't write any implementation code
 * ReactiveCrudRepository provides the standard CRUD operations with reactive
 * options (Mono and Flux return types, and more)
 * ReactiveQueryByExampleExecutor is a mix-in interface that introduces the Query by
 * Example operations which we'll poke at shortly
 */
=======

>>>>>>> 752a64f510a4c4182f346aefc94620d5396b1766
public interface EmployeeRepo extends ReactiveCrudRepository<Employee, String>, ReactiveQueryByExampleExecutor<Employee> {
}
