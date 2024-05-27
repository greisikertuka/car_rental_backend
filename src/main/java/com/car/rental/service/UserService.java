package com.car.rental.service;

import com.car.rental.model.Booking;
import com.car.rental.model.User;
import com.car.rental.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void insertUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.persist(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAllUsers() {
        List<User> usersFromDatabase = userRepository.listAll();
        for (var user : usersFromDatabase) {
            user.setPassword("");
        }
        return usersFromDatabase;
    }

    @Transactional
    public Response getUpdateProfileResponse(User user) {
        var fetcheduser = userRepository.findById(user.getId());

        //user is editing his own profile
        if (fetcheduser.getUsername().equals(user.getUsername())) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.getEntityManager().merge(user);
            return Response.status(Response.Status.NO_CONTENT).entity("{\"message\": \"Updated user successfully!\"}").build();
        }

        var existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("The username exists!").build();
        } else {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.getEntityManager().merge(user);
            return Response.status(Response.Status.NO_CONTENT).entity("{\"message\": \"Updated user successfully!\"}").build();
        }
    }


    @Transactional
    public void updateUser(User user) {
        userRepository.getEntityManager().merge(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }
}
