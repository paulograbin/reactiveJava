package com.greglturnquist.learningspringboot.learningspringboot.Example;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface EmployeeRepo extends ReactiveCrudRepository<Employee, String>, ReactiveQueryByExampleExecutor<Employee> {
}
