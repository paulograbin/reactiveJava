package com.greglturnquist.learningspringboot.learningspringboot.Example;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

public class EmployeeExample {

    @Resource
    private EmployeeRepo employeeRepository;

    public void teste() {
        Employee e = new Employee();
        e.setFirstName("Bilbo");

        final Example<Employee> exampleEmployee = Example.of(e);

        final Mono<Employee> one = employeeRepository.findOne(exampleEmployee);
    }

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
