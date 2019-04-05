package com.seto.userregistration.controller;


import com.seto.userregistration.exception.UserNotFoundException;
import com.seto.userregistration.model.User;
import com.seto.userregistration.repo.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    // tag::get-aggregate-root[]
    @GetMapping("/admin/getAllUsers")
    public List<User> getAllUsers() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/admin/createuser")
    public User createuser(@RequestBody User newEmployee) {
        return repository.save(newEmployee);
    }

    // Single item

    // tag::get-single-item[]
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        User employee = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return employee;
    }
    // end::get-single-item[]

    @PostMapping("/user/uploadpicture/{id}")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "Success";
    }


    @DeleteMapping("/admin/users/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
