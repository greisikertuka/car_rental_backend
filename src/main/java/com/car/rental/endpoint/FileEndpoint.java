package com.car.rental.endpoint;

import com.car.rental.model.ImageUploadForm;
import com.car.rental.service.FileService;
import com.car.rental.utils.Role;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/files")
@ApplicationScoped
public class FileEndpoint {

    @Inject
    FileService fileService;

    @POST
    @Path("/users/{userId}/profile-picture")
    @RolesAllowed({Role.USER, Role.ADMIN})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadUserProfileImage(@PathParam("userId") String userId, @MultipartForm ImageUploadForm form) {
        return fileService.saveUserProfilePicture(form, userId);
    }

    @POST
    @Path("/cars/{carId}/thumbnail")
    @RolesAllowed({Role.ADMIN})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadCarImage(@PathParam("carId") String carId, @MultipartForm ImageUploadForm form) {
        return fileService.saveCarPicture(form, carId);
    }

    @POST
    @Path("/cars/{carId}/photos")
    @RolesAllowed({Role.ADMIN})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleFileUploadForm(@PathParam("carId") String carId, @MultipartForm MultipartFormDataInput input) {
        String uploadedCarImages = fileService.uploadCarImages(input, carId);
        return Response.ok().entity("All files " + uploadedCarImages + " uploaded successfully.").build();
    }
}
