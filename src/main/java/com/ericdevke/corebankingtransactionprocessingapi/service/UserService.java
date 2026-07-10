package com.ericdevke.corebankingtransactionprocessingapi.service;

import com.ericdevke.corebankingtransactionprocessingapi.entity.User;
import com.ericdevke.corebankingtransactionprocessingapi.exception.DuplicateResourceException;
import com.ericdevke.corebankingtransactionprocessingapi.exception.ResourceNotFoundException;
import com.ericdevke.corebankingtransactionprocessingapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String fullName, String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("A user with this email already exists");
        }

        String customerNumber = generateNextCustomerNumber();

        User user = new User(fullName, email, customerNumber);
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow( () ->
                        new ResourceNotFoundException("User not found with id: "+ id));
    }

    private String generateNextCustomerNumber() {
        long userCount = userRepository.count();
        long nextNumber = userCount + 1;
        return String.format("%06d", nextNumber);
    }
}
