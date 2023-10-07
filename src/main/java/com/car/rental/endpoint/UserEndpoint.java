package com.car.rental.endpoint;

import com.car.rental.model.LoginRequest;
import com.car.rental.model.User;
import com.car.rental.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {

    @Inject
    UserRepository userRepository;

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        // Find the user by username
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Verify the password using BCrypt
            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                return Response.ok().entity(user).build();
            } else {
                Response.status(Response.Status.UNAUTHORIZED).entity("Please check your password!").build();
            }
        }

        // Failed login
        return Response.status(Response.Status.UNAUTHORIZED).entity("The user does not exist!").build();
    }

    @POST
    @Path("/signUp")
    @Transactional
    public Response signUp(User user) {
        // Find the user by username
        var optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("This username already exists!").build();
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.persist(user);
        return Response.ok().entity(user).build();
    }
}
