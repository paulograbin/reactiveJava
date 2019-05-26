package com.greglturnquist.learningspringboot.learningspringboot.MongoTemplate;

import com.greglturnquist.learningspringboot.learningspringboot.QueryByExample.Employee;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;


public class QueryExample {

    @Resource
    ReactiveMongoOperations mongoOperations;

    /*
        The declaration of the probe and its example is the same as shown earlier
        To create a query for one entry, we use findOne from ReactiveMongoOperations
        For the first parameter, we create a new Query, and use the byExample static helper
        to feed it the example
        For the second parameter, we tell it to return an Employee
     */
    public void teste() {
        Employee e = new Employee();
        e.setFirstName("Bilbo");
        final Example<Employee> employeeExample = Example.of(e);

        final Mono<Employee> singleEmployee = mongoOperations.findOne(new Query(byExample(e)), Employee.class);
    }
}
