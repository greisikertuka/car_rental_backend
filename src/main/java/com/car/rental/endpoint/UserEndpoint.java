package com.car.rental.endpoint;

import com.car.rental.model.LoginRequest;
import com.car.rental.model.LoginResponse;
import com.car.rental.model.User;
import com.car.rental.model.enums.Role;
import com.car.rental.repository.UserRepository;
import com.car.rental.utils.TokenService;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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


    @Inject
    TokenService tokenService;


    @POST
    @Path("/login")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Login successful!", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoginResponse.class))),
            @APIResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @APIResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
    })
    public Response login(LoginRequest loginRequest) {
        // Find the user by username
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Verify the password using BCrypt
            if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                String token = generateJwtToken(user);
                return Response.ok(new LoginResponse(token)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Please check your password!").build();
            }
        }

        // Failed login
        return Response.status(Response.Status.NOT_FOUND).entity("The user does not exist!").build();
    }

    private String generateJwtToken(User user) {
        return tokenService.generateUserToken(user);
    }

    @POST
    @Path("/signUp")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Login successful", content = @Content(mediaType = "text/plain")),
            @APIResponse(responseCode = "409", description = "This username already exists!", content = @Content(mediaType = "text/plain")),
    })
    @Transactional
    public Response signUp(User user) {
        // Find the user by username
        var optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("This username already exists!").build();
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setRole(Role.USER);
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED).entity("Created user successfully!").build();
    }

}
