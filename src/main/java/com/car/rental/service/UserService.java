package com.car.rental.service;

import com.car.rental.model.User;
import com.car.rental.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void insertUser(User user) {
        userRepository.persist(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.getEntityManager().merge(user);
    }

    public List<User> findAllUsers() {
        return userRepository.listAll();
    }
}
