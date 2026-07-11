package com.ericdevke.corebankingtransactionprocessingapi.controller;

import com.ericdevke.corebankingtransactionprocessingapi.entity.User;
import com.ericdevke.corebankingtransactionprocessingapi.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public User registerUser(@Valid @RequestBody RegisterUserRequest request) {
        return userService.registerUser(request.fullname(), request.email());
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }

    public record RegisterUserRequest(
            @NotBlank(message = "Full name is required")
            String fullname,

            @NotBlank(message = "Email is required")
            @Email(message = "Email must be a valid email address")
            String email) {}
}
