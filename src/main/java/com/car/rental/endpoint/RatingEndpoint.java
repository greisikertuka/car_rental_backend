package com.car.rental.endpoint;

import com.car.rental.model.Car;
import com.car.rental.model.Rating;
import com.car.rental.service.RatingService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ratings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RatingEndpoint {

    @Inject
    RatingService ratingService;

    @GET
    @Path("/all")
    @APIResponse(
            responseCode = "200",
            description = "List of ratings found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Rating.class)
            )
    )
    public Response getAllRatings() {
        var response = ratingService.findAllRatings();

        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/user/{userId}")
    @APIResponse(
            responseCode = "200",
            description = "List of ratings found for a user",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Rating.class)
            )
    )
    public Response getRatingsByUserId(@PathParam("userId") Long userId) {
        var response = ratingService.findRatingsByUserId(userId);
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/car/{carId}")
    @APIResponse(
            responseCode = "200",
            description = "List of ratings found for a car",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Rating.class)
            )
    )
    public Response getRatingsByCarId(@PathParam("carId") Long carId) {
        var response = ratingService.findRatingsByCarId(carId);
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/get/{id}")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Rating found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
            @APIResponse(responseCode = "404", description = "Rating not found")
    })
    public Response getRatingById(@PathParam("id") Long id) {
        var response = ratingService.findById(id);
        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/create")
    public Response insert(@QueryParam("bookingId") Long bookingId, @QueryParam("carId") Long carId, @QueryParam("userId") Long userId, Rating rating) {
        ratingService.insertRating(bookingId, userId, carId, rating);
        return Response.status(201).build();
    }

    @PUT
    @Path("/update")
    public Response update(Rating rating) {
        ratingService.updateRating(rating);
        return Response.status(201).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        ratingService.deleteRating(id);
        return Response.status(201).build();
    }
}
