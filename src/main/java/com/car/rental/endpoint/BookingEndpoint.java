package com.car.rental.endpoint;

import com.car.rental.model.Booking;
import com.car.rental.service.BookingService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookingEndpoint {

    @Inject
    BookingService bookingService;

    @GET
    @Path("/all")
    public Response getAllBookings() {
        var response = bookingService.findAllBookings();
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getBookingsByUserId(@PathParam("userId") Long userId) {
        var response = bookingService.findBookingsByUserId(userId);
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/car/{carId}")
    public Response getBookingsByCarId(@PathParam("carId") Long carId) {
        var response = bookingService.findBookingsByCarId(carId);
        return Response.status(200).entity(response).build();
    }

    @POST
    @Path("create")
    public Response insert(@QueryParam("carId") Long carId, @QueryParam("userId") Long userId, Booking booking) {
        bookingService.insertBooking(userId, carId, booking);
        return Response.status(201).build();
    }

    @PUT
    @Path("update")
    public Response update(Booking booking) {
        bookingService.updateBooking(booking);
        return Response.status(201).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        bookingService.deleteBooking(id);
        return Response.status(201).build();
    }
}
