package com.greglturnquist.learningspringboot.learningspringboot.QueryByExample;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * It's an interface declaration, meaning, we don't write any implementation code
 * ReactiveCrudRepository provides the standard CRUD operations with reactive
 * options (Mono and Flux return types, and more)
 * ReactiveQueryByExampleExecutor is a mix-in interface that introduces the Query by
 * Example operations which we'll poke at shortly
 */
public interface EmployeeRepo extends ReactiveCrudRepository<Employee, String>, ReactiveQueryByExampleExecutor<Employee> {
}
