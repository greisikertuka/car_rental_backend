package com.car.rental.endpoint;

import com.car.rental.model.Rating;
import com.car.rental.service.RatingService;

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
    public Response getAllRatings() {
        var response = ratingService.findAllRatings();
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getRatingsByUserId(@PathParam("userId") Long userId) {
        var response = ratingService.findRatingsByUserId(userId);
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/car/{carId}")
    public Response getRatingsByCarId(@PathParam("carId") Long carId) {
        var response = ratingService.findRatingsByCarId(carId);
        return Response.status(200).entity(response).build();
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
