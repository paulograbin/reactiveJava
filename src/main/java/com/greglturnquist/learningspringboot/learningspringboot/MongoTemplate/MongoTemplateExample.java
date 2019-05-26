package com.greglturnquist.learningspringboot.learningspringboot.MongoTemplate;

import com.greglturnquist.learningspringboot.learningspringboot.QueryByExample.Employee;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


public class MongoTemplateExample {

    @Resource
    ReactiveMongoOperations mongoOperations;

    /*
        The declaration of the probe and its example is the same as shown earlier
        To create a query for one entry, we use findOne from ReactiveMongoOperations
        For the first parameter, we create a new Query, and use the byExample static helper
        to feed it the example
        For the second parameter, we tell it to return an Employee
     */
    public void returnOne() {
        Employee e = new Employee();
        e.setFirstName("Bilbo");
        final Example<Employee> employeeExample = Example.of(e);

        /**
         * The declaration of the probe and its example is the same as shown earlier
         * To create a query for one entry, we use findOne from ReactiveMongoOperations
         * For the first parameter, we create a new Query, and use the byExample static helper
         * to feed it the example
         * For the second parameter, we tell it to return an Employee
         */
        final Mono<Employee> singleEmployee = mongoOperations.findOne(new Query(byExample(employeeExample)), Employee.class);
    }


    public void returnMultiple() {
        Employee e = new Employee();
        e.setFirstName("baggins");

        ExampleMatcher matcher = ExampleMatcher.matching()
        .withIgnoreCase()
        .withMatcher("lastName", startsWith())
        .withIncludeNullValues();

        final Example<Employee> employeeExample = Example.of(e, matcher);
        final Flux<Employee> employeeFlux = mongoOperations.find(new Query(byExample(employeeExample)), Employee.class);
    }

    public void anotherExample() {
        mongoOperations.findOne(query(where("firstName").is("Frodo")), Employee.class);
    }
}
