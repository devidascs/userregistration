package com.seto.userregistration.controller;


import com.seto.userregistration.exception.UserNotFoundException;
import com.seto.userregistration.model.User;
import com.seto.userregistration.repo.UserRepository;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    // tag::get-aggregate-root[]
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/user")
    public User getUser(@RequestBody User newEmployee) {
        return repository.save(newEmployee);
    }

    // Single item

    // tag::get-single-item[]
    @GetMapping("/users/{id}")
    public Resource<User> one(@PathVariable Long id) {

        User employee = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return new Resource<>(employee,
                linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("employees"));
    }
    // end::get-single-item[]


    @DeleteMapping("/users/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
