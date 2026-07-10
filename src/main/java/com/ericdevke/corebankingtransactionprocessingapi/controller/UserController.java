package com.ericdevke.corebankingtransactionprocessingapi.controller;

import com.ericdevke.corebankingtransactionprocessingapi.entity.User;
import com.ericdevke.corebankingtransactionprocessingapi.service.UserService;
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
    public User registerUser(@RequestBody RegisterUserRequest request) {
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

    public record RegisterUserRequest(String fullname, String email) {}
}
